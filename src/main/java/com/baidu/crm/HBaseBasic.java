package com.baidu.crm;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;



public class HBaseBasic {

    public static void main(String[] args) throws Exception {
    	Configuration config = HBaseConfiguration.create();
    	config.set("hbase.zookeeper.quorum", "localhost");
    	HBaseAdmin admin = new HBaseAdmin(config);
    	
		HTableDescriptor tableDescriptor = new HTableDescriptor("My_2"); 
		   tableDescriptor.addFamily(new HColumnDescriptor("column1"));  
           tableDescriptor.addFamily(new HColumnDescriptor("column2"));  
           tableDescriptor.addFamily(new HColumnDescriptor("column3"));  
           admin.createTable(tableDescriptor); 


    }

}
