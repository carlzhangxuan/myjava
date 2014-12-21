package com.baidu.crm;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Calendar;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.alibaba.fastjson.JSON;
import com.baidu.domain.CallInfo;
import com.baidu.domain.KbInfo;
import com.baidu.domain.Test;
import com.baidu.domain.TurnOutInfo;
import com.baidu.models.BaseClue;
import com.baidu.models.MergeClue;

public class HBaseMapreduce {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration config = HBaseConfiguration.create();
		config.set(
				"hbase.zookeeper.quorum",
				"cp01-rdtest-crm-int03.cp01.baidu.com,cp01-rdtest-crm-int07.cp01.baidu.com,cp01-rdtest-crm-int08.cp01.baidu.com");
		Job job = new Job(config, "ExampleSummary");
		job.setJarByClass(HBaseMapreduce.class); // class that contains mapper
													// and reducer

		Scan scan = new Scan();
		scan.setStartRow("4a6794ad6150100c608425677713269e".getBytes());
		scan.setStopRow("4a67a41ef859088a8c8c61723004be18".getBytes());
		scan.setCaching(500); // 1 is the default in Scan, which will be bad for
								// MapReduce jobs
		scan.setCacheBlocks(false); // don't set to true for MR jobs
		// set other scan attrs

		String sourceTable = "deploy_stream_table_cps_info";

		TableMapReduceUtil.initTableMapperJob(sourceTable, // input table
				scan, // Scan instance to control CF and attribute selection
				MyMapper.class, // mapper class
				Text.class, // mapper output key
				Text.class, // mapper output value
				job);

		String targetTable = "xiejun_test";
		TableMapReduceUtil.initTableReducerJob(targetTable, // output table
				MyTableReducer.class, // reducer class
				job);
		job.setNumReduceTasks(1); // at least one, adjust as required
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
	}

	public static class MyMapper extends TableMapper<Text, Text> {
		public static final byte[] CF = "all_info".getBytes();
		public static final byte[] ATTR_MERGE = "merge".getBytes();
		public static final byte[] ATTR_PG = "pg".getBytes();

		private Text myKey = new Text();
		private Text myValue = new Text();
		Calendar now = Calendar.getInstance();
		
		Timer dayCounter = new Timer();

		public void map(ImmutableBytesWritable row, Result value,
				Context context) throws IOException, InterruptedException {
			myKey.clear();
			myValue.clear();
			Map<String, String> myMergeOut = new TreeMap<String, String>();
			Map<String, String> myContextOut = new TreeMap<String, String>();
			String rowKey = new String(row.get());
			myKey.set(rowKey);

			if (value.getValue(CF, ATTR_MERGE) == null) {
				myMergeOut.put("mergeFlg", "0");
			} else {
				myMergeOut.put("mergeFlg", "1");
				String mergeVal = new String(value.getValue(CF, ATTR_MERGE));
				MergeClue mergeClue = JSON.parseObject(mergeVal,MergeClue.class);
				BaseClue baseClue = mergeClue.getDupClue().getNormClue().getBaseClue();
				String clueId = baseClue.getId();
				myMergeOut.put("clueId", clueId);
			}

			if (value.getValue(CF, ATTR_PG) == null) {
				myContextOut.put("pgFlg", "0");
			} else {
				myContextOut.put("pgFlg", "1");
				String val_pg = new String(value.getValue(CF, ATTR_PG));
				Test test = JSON.parseObject(val_pg, Test.class);
				
				//全体时间戳
				String[] oriLabels = new String[8];
				oriLabels[0] = "d7";
				oriLabels[1] = "d14";
				oriLabels[2] = "m1";
				oriLabels[3] = "m2";
				oriLabels[4] = "m3";
				oriLabels[5] = "m6";
				oriLabels[6] = "m9";
				oriLabels[7] = "m12";	
				List<String> timeLabels = new ArrayList<String>(); //day, month labels
				for (int i=0;i<oriLabels.length;i++){timeLabels.add(oriLabels[i]);}
				
				//客户资料
				String custId = test.getId();
				String hintId = test.getHint_id();// 客户资料ID
				String hintSource1 = test.getHint_source_1();// 线索来源方式-是否销售录入
				String hintSource2 = test.getHint_source_2();// 线索来源方式-是否销售录入
				String inpgTime = test.getAdd_time();// 线索入库时间
				String verifyTime = ""; // 线索清洗时间
				String hintPhone = ""; // 线索联系电话  -->todo
				String hintPhoneType = "" ;//线索联系电话类型
				String siteType = test.getSite_type(); // 网站类型 - 0已有网站1暂无网站2未知
				String noSiteType = test.getNo_site_type(); // 暂无网站说明 - 0未开始建站2协作单位建设中 3企业自建中
				int flag_check = 0; // 是否通过唯一性校验
				String check_time = ""; // 唯一性校验时间
				
				//商机日志
				List<Integer> fnD = new ArrayList<Integer>(); // 7,14,30,60,90,180,270,365天内商机次数 次数
				List<Integer> fdD = new ArrayList<Integer>(); // 7,14,30,60,90,180,270,365天内商机天数 天数 
				/*Set<String> fpisD7 = new HashSet<String>(); // 7天内商机IS人数 
				Set<String> fpisD14 = new HashSet<String>(); // 14天内商机IS人数
				Set<String> fpisD30 = new HashSet<String>(); // 30天内商机IS人数
				Set<String> fpisD60 = new HashSet<String>(); // 60天内商机IS人数
				Set<String> fpisD90 = new HashSet<String>(); // 90天内商机IS人数
				Set<String> fpisD180 = new HashSet<String>(); // 180天内商机IS人数
				Set<String> fpisD270 = new HashSet<String>(); // 270天内商机IS人数
				Set<String> fpisD365 = new HashSet<String>(); // 365天内商机IS人数*/
				List<Integer> annD = new ArrayList<Integer>(); // 7,14,30,60,90,180,270,365天内申请废弃次数 -->N/A		
				for(int i=0;i<timeLabels.size();i++){fnD.add(0);fdD.add(0);annD.add(0);}
				String ckb_time = ""; //当前商机时间
				String ckb_status = "";//当前商机状态		
				String fkb_time = "";//首次商机（客保）时间
				int fkb_days = 0; // 首次商机天数
				String lkb_time = "";//末次商机（客保）时间
				int lkb_days = 0; // 末次商机天数
				String fkb_os_time = "";//首次商机派单时间
				String chengdan_flag = ""; //是否成单
				
				//转接单日志
				List<Integer>onD = new ArrayList<Integer>();//7,14,30,60,90,180,270,365天内派out次数
				Set<String>fpos_d7 = new HashSet<String>();//7天内商机OS人数
				Set<String>fpos_d14 = new HashSet<String>();//14天内商机OS人数
				Set<String>fpos_m1 = new HashSet<String>();//30天内商机OS人数
				Set<String>fpos_m2 = new HashSet<String>();//60天内商机OS人数
				Set<String>fpos_m3 = new HashSet<String>();//90天内商机OS人数
				Set<String>fpos_m6 = new HashSet<String>();//180天内商机OS人数
				Set<String>fpos_m9 = new HashSet<String>();//270天内商机OS人数
				Set<String>fpos_m12 = new HashSet<String>();//365天内商机OS人数
				int fkb_os_flag = 0;//首次商机是否OS跟进
				int fkb_os_cn = 0;//首次商机OS跟进次数
				for(int i=0;i<timeLabels.size();i++){onD.add(0);}
				
				//呼叫中心日志
				int cn_d7 = 0; // 7天内拨打次数 次数
				int cn_d14 = 0; // 14天内拨打次数 次数
				int cn_m1 = 0; // 30天内拨打次数 次数
				int cn_m2 = 0; // 60天内拨打次数 次数
				int cn_m3 = 0; // 90天内拨打次数 次数
				int cn_m6 = 0; // 180天内拨打次数 次数
				int cn_m9 = 0; // 270天内拨打次数 次数
				int cn_m12 = 0; // 365天内拨打次数  次数
				int dura_d7 = 0; // 7天内拨打时长 秒 - calldata.call_duration 通话时长
				int dura_d14 = 0; // 14天内拨打时长 秒
				int dura_m1 = 0; // 30天内拨打时长 秒
				int dura_m2 = 0; // 60天内拨打时长 秒
				int dura_m3 = 0; // 90天内拨打时长 秒
				int dura_m6 = 0; // 180天内拨打时长 秒
				int dura_m9 = 0; // 270天内拨打时长 秒
				int dura_m12 = 0; // 365天内拨打时长 秒
				int cnis_d7 = 0; // 7天内IS拨打次数
				int cnis_d14 = 0; // 7天内IS拨打次数
				int cnis_m1 = 0; // 30天内IS拨打次数
				int cnis_m2 = 0; // 60天内IS拨打次数
				int cnis_m3 = 0; // 90天内IS拨打次数
				int cnis_m6 = 0; // 180天内IS拨打次数
				int cnis_m9 = 0; // 270天内IS拨打次数
				int cnis_m12 = 0; // 365天内IS拨打次数
				Set<String> cpnis_d7 = new HashSet<String>();// 7天内IS拨打人数 
				Set<String> cpnis_d14 = new HashSet<String>();// 14天内IS拨打人数 
				Set<String> cpnis_m1 = new HashSet<String>();// 30天内IS拨打人数 
				Set<String> cpnis_m2 = new HashSet<String>();// 60天内IS拨打人数 
				Set<String> cpnis_m3 = new HashSet<String>();// 90天内IS拨打人数 
				Set<String> cpnis_m6 = new HashSet<String>();// 180天内IS拨打人数 
				Set<String> cpnis_m9 = new HashSet<String>();// 270天内IS拨打人数 
				Set<String> cpnis_m12 = new HashSet<String>();// 365天内IS拨打人数 
				int cnv_d7 = 0; // 7天内IS接通次数
				int cnv_d14 = 0; // 14天内IS接通次数
				int cnv_m1 = 0; // 30天内IS接通次数
				int cnv_m2 = 0; // 60天内IS接通次数
				int cnv_m3 = 0; // 90天内IS接通次数
				int cnv_m6 = 0; // 180天内IS接通次数
				int cnv_m9 = 0; // 270天内IS接通次数
				int cnv_m12 = 0; // 365天内IS接通次数
				List<Integer> mdis_d7 = new ArrayList<Integer>(); //7天内IS最大拨打时长
				List<Integer> mdis_d14 = new ArrayList<Integer>(); //14天内IS最大拨打时长
				List<Integer> mdis_m1 = new ArrayList<Integer>(); //30天内IS最大拨打时长
				List<Integer> mdis_m2 = new ArrayList<Integer>(); //60天内IS最大拨打时长
				List<Integer> mdis_m3 = new ArrayList<Integer>(); //90天内IS最大拨打时长
				List<Integer> mdis_m6 = new ArrayList<Integer>(); //180天内IS最大拨打时长
				List<Integer> mdis_m9 = new ArrayList<Integer>(); //270天内IS最大拨打时长
				List<Integer> mdis_m12 = new ArrayList<Integer>(); //365天内IS最大拨打时长
				List<String> ckb_flca_time = new ArrayList<String>(); //当前商机首次拨打时间,当前商机末次拨打时间
				int ckb_is_ccn = 0; // 当前商机IS拨打次数
				int ckb_is_ccvn = 0; // 当前商机IS接通次数
				int ckb_dura = 0; // 当前商机累计拨打时长
				List <Integer>ckb_mdura = new ArrayList<Integer>(); // 当前商机最大通话时长
				String fkb_fca_time ="";//首次商机拨打时间
				int fkb_is_ccn = 0; // 首次商机IS拨打次数
				int fkb_is_ccvn = 0; // 首次商机IS接通次数
				int fkb_dura = 0;  // 首次商机累计拨打时长
				List<Integer> fkb_mdura = new ArrayList<Integer>();// 首次商机最大通话时长 
				int lkb_is_ccn = 0; // 末次商机IS拨打次数
				int lkb_is_ccvn = 0; // 末次商机IS接通次数
				int lkb_dura = 0;  // 末次商机累计拨打时长
				List<Integer> lkb_mdura = new ArrayList<Integer>();// 首次商机最大通话时长 
				List<Integer> call_drua_1_5 = new ArrayList<Integer>(); // 第n(n<5)次拨打时长
				int avg_call_dura5 = 0; //前5次平均拨打时长  
				double std_call_dura5 = 0. ;//前N(N<=5)次拨打时长标准差
				int dist_first_call = 0;//第一次拨打时间距统计期间隔
				int dist_last_call = 0;//最近一次拨打时间距统计期间隔
				int dist_call_start = 0;//拨打与接通时间平均间隔
				List<Integer> dist_call_start1_5 = new ArrayList<Integer>(); // 第1次拨打与接通时间间隔
				int avg_dist_call_start5 = 0;//前5次拨打与接通时间间隔均值
				double std_dist_call_start5 = 0.;//前5次拨打与接通时间间隔标准差
				List<Integer> call_drua_r1_5 = new ArrayList<Integer>(); // 最近n(n<=5)次拨打时长
				int avg_call_dura_r5 = 0;//最近前5次平均拨打时长
				int[] cn_d7s = new int[5];for(int i=0; i<5; i++){cn_d7s[i] = 0;}  //7天内通话时长在（xx）秒次数
				int[] cn_d14s = new int[5];for(int i=0; i<5; i++){cn_d14s[i] = 0;}//14天内通话时长在(xx）秒次数
				int[] cn_m1s = new int[5];for(int i=0; i<5; i++){cn_m1s[i] = 0;}//30天内通话时长在（xx）秒次数
				int[] cn_m2s = new int[5];for(int i=0; i<5; i++){cn_m2s[i] = 0;}//60天内通话时长在（xx）秒次数
				int[] cn_m3s = new int[5];for(int i=0; i<5; i++){cn_m3s[i] = 0;}//90天内通话时长在（xx）秒次数
				int[] cn_m6s = new int[5];for(int i=0; i<5; i++){cn_m6s[i] = 0;}//180天内通话时长在（xx）秒次数
				int[] cn_m9s = new int[5];for(int i=0; i<5; i++){cn_m9s[i] = 0;}//270天内通话时长在（xx）秒次数
				int[] cn_m12s = new int[5];for(int i=0; i<5; i++){cn_m12s[i] = 0;}//365天内通话时长在（xx）秒次数
				Set<String> cpn_d7s0 = new HashSet<String>();//7天内通话时长在（20）秒人数
				Set<String> cpn_d7s20 = new HashSet();//7天内通话时长在（60）秒人数
				Set<String> cpn_d7s60 = new HashSet();//7天内通话时长在（180）秒人数
				Set<String> cpn_d7s180 = new HashSet();//7天内通话时长在（>180）秒人数
				Set<String> cpn_d14s0 = new HashSet();//14天内通话时长在（20）秒人数
				Set<String> cpn_d14s20 = new HashSet();//14天内通话时长在（60）秒人数
				Set<String> cpn_d14s60 = new HashSet();//14天内通话时长在（180）秒人数
				Set<String> cpn_d14s180 = new HashSet();//14天内通话时长在（>180）秒人数
				Set<String> cpn_m1s0 = new HashSet();//30天内通话时长在（20）秒人数
				Set<String> cpn_m1s20 = new HashSet();//30天内通话时长在（60）秒人数
				Set<String> cpn_m1s60 = new HashSet();//30天内通话时长在（180）秒人数
				Set<String> cpn_m1s180 = new HashSet();//30天内通话时长在（>180）秒人数
				Set<String> cpn_m2s0 = new HashSet();//60天内通话时长在（20）秒人数
				Set<String> cpn_m2s20 = new HashSet();//60天内通话时长在（60）秒人数
				Set<String> cpn_m2s60 = new HashSet();//60天内通话时长在（180）秒人数
				Set<String> cpn_m2s180 = new HashSet();//60天内通话时长在（>180）秒人数
				Set<String> cpn_m3s0 = new HashSet();//90天内通话时长在（20）秒人数
				Set<String> cpn_m3s20 = new HashSet();//90天内通话时长在（90）秒人数
				Set<String> cpn_m3s60 = new HashSet();//90天内通话时长在（180）秒人数
				Set<String> cpn_m3s180 = new HashSet();//90天内通话时长在（>180）秒人数
				Set<String> cpn_m6s0 = new HashSet();//180天内通话时长在（20）秒人数
				Set<String> cpn_m6s20 = new HashSet();//180天内通话时长在（60）秒人数
				Set<String> cpn_m6s60 = new HashSet();//180天内通话时长在（180）秒人数
				Set<String> cpn_m6s180 = new HashSet();//180天内通话时长在（>180）秒人数
				Set<String> cpn_m9s0 = new HashSet();//270天内通话时长在（20）秒人数
				Set<String> cpn_m9s20 = new HashSet();//270天内通话时长在（60）秒人数
				Set<String> cpn_m9s60 = new HashSet();//270天内通话时长在（180）秒人数
				Set<String> cpn_m9s180 = new HashSet();//270天内通话时长在（>180）秒人数
				Set<String> cpn_m12s0 = new HashSet();//365天内通话时长在（20）秒人数
				Set<String> cpn_m12s20 = new HashSet();//365天内通话时长在（60）秒人数
				Set<String> cpn_m12s60 = new HashSet();//365天内通话时长在（180）秒人数
				Set<String> cpn_m12s180 = new HashSet();//365天内通话时长在（>180）秒人数
						
				int firstCallFlag = 0;
				int turnOutFlag = 0;
				int callCount = 0;
				
				if (!CollectionUtils.isEmpty(test.getKb_info())) {
					int sjCount = 0;
					int sjLength = test.getKb_info().size();
					for (KbInfo shangJi : test.getKb_info()) {//每次商机过程中含有多个callinfo
						
						String sjAddTime = shangJi.getAdd_time().split("\\.")[0];
						String sjEndTime;
						int deltaDayIn;//本次商机天数
						if (shangJi.getClose_time() != null){sjEndTime = shangJi.getClose_time();}
						else if(shangJi.getUpd_time() != null){sjEndTime = shangJi.getUpd_time();}
						else{sjEndTime = "";}
						int deltaDaySj = dayCounter.daysFromNow(now, sjAddTime);//商机开始到现在的天数
						if (!sjEndTime.equals("")){deltaDayIn = dayCounter.daysBetween(sjEndTime, sjAddTime);}
						else {deltaDayIn = deltaDaySj;}
						
						if(sjCount == 0){
							fkb_time = sjAddTime;;//首次商机（客保）时间
							fkb_days = deltaDayIn;//首次商机天数
							}
						
						if(sjCount == sjLength-2){
							lkb_time = sjAddTime;;//首次商机（客保）时间
							lkb_days = deltaDayIn;//首次商机天数
							}
						
						if(sjCount == sjLength-1){
							ckb_time = sjAddTime;//当前商机时间
							ckb_status = shangJi.getStat();//当前商机状态
							chengdan_flag = shangJi.getContract_flag();//是否成单				
						}
																								
						if(deltaDaySj<=7){fnD.set(0, fnD.get(0)+1);fdD.set(0, fdD.get(0)+deltaDayIn);}
						else if (deltaDaySj<=14){fnD.set(1, fnD.get(1)+1);fdD.set(1, fdD.get(1)+deltaDayIn);}
						else if (deltaDaySj<=30){fnD.set(2, fnD.get(2)+1);fdD.set(2, fdD.get(2)+deltaDayIn);}
						else if (deltaDaySj<=60){fnD.set(3, fnD.get(3)+1);fdD.set(3, fdD.get(3)+deltaDayIn);}
						else if (deltaDaySj<=90){fnD.set(4, fnD.get(4)+1);fdD.set(4, fdD.get(4)+deltaDayIn);}
						else if (deltaDaySj<=180){fnD.set(5, fnD.get(5)+1);fdD.set(5, fdD.get(5)+deltaDayIn);}
						else if (deltaDaySj<=270){fnD.set(6, fnD.get(6)+1);fdD.set(6, fdD.get(6)+deltaDayIn);}
						else{fnD.set(7, fnD.get(7)+1);fdD.set(7, fdD.get(7)+deltaDayIn);}
						
						//TURNOUT LOOP
						if(!CollectionUtils.isEmpty(shangJi.getTurn_out_infos())){
							for (TurnOutInfo turnOutInfo : shangJi.getTurn_out_infos()){
								String turnOutTime = turnOutInfo.getTurn_out_time().split("\\.")[0];
								String osId = turnOutInfo.getOut_posid();
								if(turnOutFlag == 0){fkb_os_time = turnOutTime;} 
								int deltaDaysTO = dayCounter.daysFromNow(now, turnOutTime);
								
								if(deltaDaysTO<=7){onD.set(0, onD.get(0)+1);fpos_d7.add(osId);}
								else if(deltaDaysTO<=14){onD.set(1, onD.get(1)+1);fpos_d14.add(osId);}
								else if(deltaDaysTO<=30){onD.set(2, onD.get(2)+1);fpos_m1.add(osId);}
								else if(deltaDaysTO<=60){onD.set(3, onD.get(3)+1);fpos_m2.add(osId);}
								else if(deltaDaysTO<=90){onD.set(4, onD.get(4)+1);fpos_m3.add(osId);}
								else if(deltaDaysTO<=180){onD.set(5, onD.get(5)+1);fpos_m6.add(osId);}
								else if(deltaDaysTO<=270){onD.set(6, onD.get(6)+1);fpos_m9.add(osId);}
								else if(deltaDaysTO<=365){onD.set(7, onD.get(7)+1);fpos_m12.add(osId);} 
								}
							}
						//CALL LOOP			
						if (!CollectionUtils.isEmpty(shangJi.getCallInfos())) {
							for (CallInfo callInfo : shangJi.getCallInfos()) {//每个callinfo含有一个IS/OSID
								int callDur;
								String agent_type = callInfo.getAgent_type();
								String add_ucid = callInfo.getAdd_ucid();
								String connection_time = callInfo.getConnection_time();
								String create_time = callInfo.getCreate_time().split("\\.")[0];
								ckb_flca_time.add(create_time);
								int call_duration = Integer.valueOf(callInfo.getCall_duration()).intValue();
								if (call_duration == -1){callDur = 0;}else{callDur = call_duration;}
								ckb_mdura.add(callDur);
								
								if(sjCount == 0){
									if(firstCallFlag == 0){
										fkb_fca_time = create_time;
										firstCallFlag = 1;
									}
									fkb_is_ccn += 1;	
									if(callDur != 0){
										fkb_is_ccvn += 1;
										fkb_dura += callDur;
										fkb_mdura.add(callDur);
									}
								}
								
								if(sjCount == sjLength-1){
									lkb_is_ccn += 1;
									if(callDur != 0){
										lkb_is_ccvn += 1;
										lkb_dura += callDur;
										lkb_mdura.add(callDur);
									}									
								}
								
								if(callCount <5){
									if(callDur != 0){
										call_drua_1_5.add(callDur);
										callCount += 1;
									}
								}
								
								if(call_drua_r1_5.size()<=5){
									if(callDur != 0){
										call_drua_r1_5.add(callDur);
									}
								}else{
									call_drua_r1_5.add(callDur);
									call_drua_r1_5.remove(0);
								}
															
								int deltaDay = dayCounter.daysFromNow(now, create_time);

								if(deltaDay<= 7){cn_d7+= 1;dura_d7+=callDur;
									if(callDur<=20){cn_d7s[0]+=1;cpn_d7s0.add(add_ucid);}
									else if(callDur<=60){cn_d7s[1]+=1;cpn_d7s20.add(add_ucid);}
									else if(callDur<=180){cn_d7s[2]+=1;cpn_d7s60.add(add_ucid);}
									else{cn_d7s[3]+=1;cpn_d7s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_d7+=1;cpnis_d7.add(add_ucid);
										if(connection_time!=null){cnv_d7+=1;mdis_d7.add(callDur);}}}
								else if(deltaDay<=14){cn_d14+=1;dura_d14+=callDur;
									if(callDur<=20){cn_d14s[0]+=1;cpn_d14s0.add(add_ucid);}
									else if(callDur<=60){cn_d14s[1]+=1;cpn_d14s20.add(add_ucid);}
									else if(callDur<=180){cn_d14s[2]+=1;cpn_d14s60.add(add_ucid);}
									else{cn_d14s[3]+=1;cpn_d14s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_d14+=1;cpnis_d14.add(add_ucid);
										if(connection_time!=null){cnv_d14+=1;mdis_d14.add(callDur);}}}
								else if(deltaDay<= 30){cn_m1+=1;dura_m1+=callDur;
									if(callDur<=20){cn_m1s[0]+=1;cpn_m1s0.add(add_ucid);}
									else if(callDur<=60){cn_m1s[1]+=1;cpn_m1s20.add(add_ucid);}
									else if(callDur<=180){cn_m1s[2]+=1;cpn_m1s60.add(add_ucid);}
									else{cn_m1s[3]+=1;cpn_m1s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_m1+=1;cpnis_m1.add(add_ucid);
										if(connection_time!=null){cnv_m1+=1;mdis_m1.add(callDur);}}}
								else if(deltaDay<= 60){cn_m2+=1;dura_m2+=callDur;
									if(callDur<=20){cn_m2s[0]+=1;cpn_m2s0.add(add_ucid);}
									else if(callDur<=60){cn_m2s[1]+=1;cpn_m2s20.add(add_ucid);}
									else if(callDur<=180){cn_m2s[2]+=1;cpn_m2s60.add(add_ucid);}
									else{cn_m2s[3]+=1;cpn_m2s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_m2+=1;cpnis_m2.add(add_ucid);
										if(connection_time!=null){cnv_m2+=1;mdis_m2.add(callDur);}}}
								else if(deltaDay<= 90){cn_m3+=1;dura_m3+=callDur;
									if(callDur<=20){cn_m3s[0]+=1;cpn_m3s0.add(add_ucid);}
									else if(callDur<=60){cn_m3s[1]+=1;cpn_m3s20.add(add_ucid);}
									else if(callDur<=180){cn_m3s[2]+=1;cpn_m3s60.add(add_ucid);}
									else{cn_m3s[3]+=1;cpn_m3s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_m3+= 1;cpnis_m3.add(add_ucid);
										if(connection_time!=null){cnv_m3+=1;mdis_m3.add(callDur);}}}
								else if(deltaDay<= 180){cn_m6+=1;dura_m6+=callDur;
									if(callDur<=20){cn_m6s[0]+=1;cpn_m6s0.add(add_ucid);}
									else if(callDur<=60){cn_m6s[1]+=1;cpn_m6s20.add(add_ucid);}
									else if(callDur<=180){cn_m6s[2]+=1;cpn_m6s60.add(add_ucid);}
									else{cn_m6s[3]+=1;cpn_m6s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_m6+=1;cpnis_m6.add(add_ucid);
										if(connection_time!=null){cnv_m6+=1;mdis_m6.add(callDur);}}}
								else if(deltaDay<= 270){cn_m9+=1;dura_m9+=callDur;
									if(callDur<=20){cn_m9s[0]+=1;cpn_m9s0.add(add_ucid);}
									else if(callDur<=60){cn_m9s[1]+=1;cpn_m9s20.add(add_ucid);}
									else if(callDur<=180){cn_m9s[2]+=1;cpn_m9s60.add(add_ucid);}
									else{cn_m9s[3]+=1;cpn_m9s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_m9+=1;cpnis_m9.add(add_ucid);
										if(connection_time!=null){cnv_m9+=1;mdis_m9.add(callDur);}}}
								else if(deltaDay<= 365){cn_m12+=1;dura_m12+=callDur;
									if(callDur<=20){cn_m12s[0]+=1;cpn_m12s0.add(add_ucid);}
									else if(callDur<=60){cn_m12s[1]+=1;cpn_m12s20.add(add_ucid);}
									else if(callDur<=180){cn_m12s[2]+=1;cpn_m12s60.add(add_ucid);}
									else{cn_m12s[3]+=1;cpn_m12s180.add(add_ucid);}
									if(agent_type.equals("is")){cnis_m12+=1;cpnis_m12.add(add_ucid);
										if(connection_time!=null){cnv_m12+=1;mdis_m12.add(callDur);}}}
							}

							sjCount+=1;
						
						} else {

						}
					}
					if(!CollectionUtils.isEmpty(mdis_d7)){Collections.sort(mdis_d7);}else{mdis_d7.add(0);}
					if(!CollectionUtils.isEmpty(mdis_d14)){Collections.sort(mdis_d14);}else{mdis_d14.add(0);}
					if(!CollectionUtils.isEmpty(mdis_m1)){Collections.sort(mdis_m1);}else{mdis_m1.add(0);}
					if(!CollectionUtils.isEmpty(mdis_m2)){Collections.sort(mdis_m2);}else{mdis_m2.add(0);}
					if(!CollectionUtils.isEmpty(mdis_m3)){Collections.sort(mdis_m3);}else{mdis_m3.add(0);}
					if(!CollectionUtils.isEmpty(mdis_m6)){Collections.sort(mdis_m6);}else{mdis_m6.add(0);}
					if(!CollectionUtils.isEmpty(mdis_m9)){Collections.sort(mdis_m9);}else{mdis_m9.add(0);}
					if(!CollectionUtils.isEmpty(mdis_m12)){Collections.sort(mdis_m12);}else{mdis_m12.add(0);}
					
					if(!CollectionUtils.isEmpty(ckb_flca_time)){Collections.sort(ckb_flca_time);}else{ckb_flca_time.add("");}
					
					ckb_is_ccn = cnis_d7 + cnis_d14 +cnis_m1 + cnis_m2 + cnis_m3 + cnis_m6 + cnis_m9 + cnis_m12; 
					ckb_is_ccvn = cnv_d7 + cnv_d14 + cnv_m1 + cnv_m2 + cnv_m3 + cnv_m6 + cnv_m9 + cnv_m12;
					ckb_dura = dura_d7 + dura_d14 + dura_m1 + dura_m2 + dura_m3 + dura_m6 + dura_m9 + dura_m12;
					if(!CollectionUtils.isEmpty(ckb_mdura)){Collections.sort(ckb_mdura);}else{ckb_mdura.add(0);}
					
					if(!CollectionUtils.isEmpty(fkb_mdura)){Collections.sort(fkb_mdura);}else{fkb_mdura.add(0);}
					if(!CollectionUtils.isEmpty(lkb_mdura)){Collections.sort(lkb_mdura);}else{lkb_mdura.add(0);}
					
					//商机日志
					for(int i=0;i<fnD.size();i++){
						myContextOut.put("fn_"+timeLabels.get(i), fnD.get(i)+"");
						myContextOut.put("fd_"+timeLabels.get(i), fdD.get(i)+"");
						}
					myContextOut.put("ckb_time", ckb_time + "");
					myContextOut.put("ckb_status", ckb_status);
					myContextOut.put("fkb_time", fkb_time);
					myContextOut.put("fkb_days", fkb_days +"");
					myContextOut.put("lkb_time", lkb_time);
					myContextOut.put("lkb_days", lkb_days +"");
					myContextOut.put("fkb_os_time", fkb_os_time);
					myContextOut.put("chengdan_flag", chengdan_flag);
					
					//转接日志
					for(int i=0;i<onD.size();i++){
						myContextOut.put("on_"+timeLabels.get(i), onD.get(i)+"");
						}
					myContextOut.put("fpos_d7", fpos_d7.size() + "");
					myContextOut.put("fpos_d14", fpos_d14.size() + "");
					myContextOut.put("fpos_m1", fpos_m1.size() + "");
					myContextOut.put("fpos_m2", fpos_m2.size() + "");
					myContextOut.put("fpos_m3", fpos_m3.size() + "");
					myContextOut.put("fpos_m6", fpos_m6.size() + "");
					myContextOut.put("fpos_m9", fpos_m9.size() + "");
					myContextOut.put("fpos_m12", fpos_m12.size() + "");
					
					//呼叫中心日志
					myContextOut.put("cn_d7", cn_d7 + "");
					myContextOut.put("cn_d14", cn_d14 + "");
					myContextOut.put("cn_m1", cn_m1 + "");
					myContextOut.put("cn_m2", cn_m2 + "");
					myContextOut.put("cn_m3", cn_m3 + "");
					myContextOut.put("cn_m6", cn_m6 + "");
					myContextOut.put("cn_m9", cn_m9 + "");
					myContextOut.put("cn_m12", cn_m12 + "");
					myContextOut.put("dura_d7", dura_d7 + "");
					myContextOut.put("dura_d14", dura_d14 + "");
					myContextOut.put("dura_m1", dura_m1 + "");
					myContextOut.put("dura_m2", dura_m2 + "");
					myContextOut.put("dura_m3", dura_m3 + "");
					myContextOut.put("dura_m6", dura_m6 + "");
					myContextOut.put("dura_m9", dura_m9 + "");
					myContextOut.put("dura_m12", dura_m12 + "");
					myContextOut.put("cnis_d7", cnis_d7 + "");
					myContextOut.put("cnis_d14", cnis_d14 + "");
					myContextOut.put("cnis_m1", cnis_m1 + "");
					myContextOut.put("cnis_m2", cnis_m2 + "");
					myContextOut.put("cnis_m3", cnis_m3 + "");
					myContextOut.put("cnis_m6", cnis_m6 + "");
					myContextOut.put("cnis_m9", cnis_m9 + "");
					myContextOut.put("cnis_m12", cnis_m12 + "");
					myContextOut.put("cpnis_d7", cpnis_d7.size() + "");
					myContextOut.put("cpnis_d14", cpnis_d14.size() + "");
					myContextOut.put("cpnis_m1", cpnis_m1.size() + "");
					myContextOut.put("cpnis_m2", cpnis_m2.size() + "");
					myContextOut.put("cpnis_m3", cpnis_m3.size() + "");
					myContextOut.put("cpnis_m6", cpnis_m6.size() + "");
					myContextOut.put("cpnis_m9", cpnis_m9.size() + "");
					myContextOut.put("cpnis_m12", cpnis_m12.size() + "");
					myContextOut.put("cnv_d7", cnv_d7 + "");
					myContextOut.put("cnv_d14", cnv_d14 + "");
					myContextOut.put("cnv_m1", cnv_m1 + "");
					myContextOut.put("cnv_m2", cnv_m2 + "");
					myContextOut.put("cnv_m3", cnv_m3 + "");
					myContextOut.put("cnv_m6", cnv_m6 + "");
					myContextOut.put("cnv_m9", cnv_m9 + "");
					myContextOut.put("cnv_m12", cnv_m12 + "");
					myContextOut.put("mdis_d7", mdis_d7.get(mdis_d7.size()-1) + "");
					myContextOut.put("mdis_d14", mdis_d14.get(mdis_d14.size()-1) + "");
					myContextOut.put("mdis_m1", mdis_m1.get(mdis_m1.size()-1) + "");
					myContextOut.put("mdis_m2", mdis_m2.get(mdis_m2.size()-1) + "");
					myContextOut.put("mdis_m3", mdis_m3.get(mdis_m3.size()-1) + "");
					myContextOut.put("mdis_m6", mdis_m6.get(mdis_m6.size()-1) + "");
					myContextOut.put("mdis_m9", mdis_m9.get(mdis_m9.size()-1) + "");
					myContextOut.put("mdis_m12", mdis_m12.get(mdis_m12.size()-1) + "");
					myContextOut.put("ckb_fca_time", ckb_flca_time.get(0) + "");
					myContextOut.put("ckb_lca_time", ckb_flca_time.get(ckb_flca_time.size()-1) + "");
					myContextOut.put("ckb_is_ccn", ckb_is_ccn + "");
					myContextOut.put("ckb_is_ccvn", ckb_is_ccvn + "");
					myContextOut.put("ckb_dura", ckb_dura + "");
					myContextOut.put("ckb_mdura", ckb_mdura.get(ckb_mdura.size()-1) + "");
					myContextOut.put("fkb_fca_time", fkb_fca_time + "");
					myContextOut.put("fkb_is_ccn", fkb_is_ccn + "");
					myContextOut.put("fkb_is_ccvn", fkb_is_ccvn + "");
					myContextOut.put("fkb_dura", fkb_dura + "");
					myContextOut.put("fkb_mdura", fkb_mdura.get(fkb_mdura.size()-1) + "");
					myContextOut.put("lkb_is_ccn", lkb_is_ccn + "");
					myContextOut.put("lkb_is_ccvn", lkb_is_ccvn + "");
					myContextOut.put("lkb_dura", lkb_dura + "");
					myContextOut.put("lkb_mdura", lkb_mdura.get(lkb_mdura.size()-1) + "");
					for (int i=0; i<call_drua_1_5.size(); i++){
						myContextOut.put("call_drua_"+(i+1), call_drua_1_5.get(i) + "");
						avg_call_dura5 += call_drua_1_5.get(i);
						}
					avg_call_dura5 = avg_call_dura5/call_drua_1_5.size();
					myContextOut.put("avg_call_dura5", avg_call_dura5 + "");
					myContextOut.put("std_call_dura5", ""); //todo
					myContextOut.put("dist_first_call", ""); //todo
					myContextOut.put("dist_last_call", ""); //todo
					//...dist todo
					for (int i=0; i<call_drua_r1_5.size(); i++){
						myContextOut.put("call_drua_r"+(i+1), call_drua_r1_5.get(i) + "");
						avg_call_dura_r5 += call_drua_r1_5.get(i);
						}
					avg_call_dura_r5 = avg_call_dura_r5/call_drua_r1_5.size();
					myContextOut.put("avg_call_dura_r5", avg_call_dura_r5 + "");
					for (int i=0; i<4;i++){
						myContextOut.put("cn_d7s"+i, cn_d7s[i]+"");
						myContextOut.put("cn_d14s"+i, cn_d14s[i]+"");
						myContextOut.put("cn_m1s"+i, cn_m1s[i]+"");
						myContextOut.put("cn_m2s"+i, cn_m2s[i]+"");
						myContextOut.put("cn_m3s"+i, cn_m3s[i]+"");
						myContextOut.put("cn_m6s"+i, cn_m6s[i]+"");
						myContextOut.put("cn_m9s"+i, cn_m9s[i]+"");
						myContextOut.put("cn_m12s"+i, cn_m12s[i]+"");
					}
					myContextOut.put("cpn_d7s0", cpn_d7s0.size()+"");
					myContextOut.put("cpn_d7s20", cpn_d7s20.size()+"");
					myContextOut.put("cpn_d7s60", cpn_d7s60.size()+"");
					myContextOut.put("cpn_d7s180", cpn_d7s180.size()+"");
					myContextOut.put("cpn_d14s0", cpn_d14s0.size()+"");
					myContextOut.put("cpn_d14s20", cpn_d14s20.size()+"");
					myContextOut.put("cpn_d14s60", cpn_d14s60.size()+"");
					myContextOut.put("cpn_d14s180", cpn_d14s180.size()+"");
					myContextOut.put("cpn_m1s0", cpn_m1s0.size()+"");
					myContextOut.put("cpn_m1s20", cpn_m1s20.size()+"");
					myContextOut.put("cpn_m1s60", cpn_m1s60.size()+"");
					myContextOut.put("cpn_m1s180", cpn_m1s180.size()+"");
					myContextOut.put("cpn_m2s0", cpn_m2s0.size()+"");
					myContextOut.put("cpn_m2s20", cpn_m2s20.size()+"");
					myContextOut.put("cpn_m2s60", cpn_m2s60.size()+"");
					myContextOut.put("cpn_m2s180", cpn_m2s180.size()+"");
					myContextOut.put("cpn_m3s0", cpn_m3s0.size()+"");
					myContextOut.put("cpn_m3s20", cpn_m3s20.size()+"");
					myContextOut.put("cpn_m3s60", cpn_m3s60.size()+"");
					myContextOut.put("cpn_m3s180", cpn_m3s180.size()+"");
					myContextOut.put("cpn_m6s0", cpn_m6s0.size()+"");
					myContextOut.put("cpn_m6s20", cpn_m6s20.size()+"");
					myContextOut.put("cpn_m6s60", cpn_m6s60.size()+"");
					myContextOut.put("cpn_m6s180", cpn_m6s180.size()+"");
					myContextOut.put("cpn_m9s0", cpn_m9s0.size()+"");
					myContextOut.put("cpn_m9s20", cpn_m9s20.size()+"");
					myContextOut.put("cpn_m9s60", cpn_m9s60.size()+"");
					myContextOut.put("cpn_m9s180", cpn_m9s180.size()+"");
					myContextOut.put("cpn_m12s0", cpn_m12s0.size()+"");
					myContextOut.put("cpn_m12s20", cpn_m12s20.size()+"");
					myContextOut.put("cpn_m12s60", cpn_m12s60.size()+"");
					myContextOut.put("cpn_m12s180", cpn_m12s180.size()+"");
					
					
				} else {

				}
			}
			//map OUtPUT
			Map<String, Object> mapOut = new TreeMap<String, Object>();
			mapOut.put("pg", myContextOut);
			mapOut.put("merge", myMergeOut);
			String mapOutJson = JSON.toJSONString(mapOut);
			myValue.set(mapOutJson);
			context.write(myKey, myValue);
		}
	}

	public static class MyTableReducer extends TableReducer<Text, Text, Text> {
		public static final byte[] CF = "test1".getBytes();
		public static final byte[] COUNT = "info22".getBytes();

		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			Iterator<Text> iterator = values.iterator();
			while (iterator.hasNext()) {
				Put put = new Put(key.getBytes());
				put.add(CF, COUNT, iterator.next().getBytes());
				context.write(key, put);
			}
		}
	}

}
