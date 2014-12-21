package com.baidu.models;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 单个地域元素类
 * 
 * @author tangchunsong
 * 
 */
public class Place {

	private static Logger LOG = Logger.getLogger(Place.class);

	/**
	 * 地域级别
	 * 
	 * @author tangchunsong
	 */
	public enum PlaceLevel {
		prov(0), // 省级(含直辖市)
		city(1), // 市级(含直辖市)
		county(2); // 区县级(含直辖市的区县)

		private int _levelId = 0;

		private PlaceLevel(int levelId) {
			_levelId = levelId;
		}

		public int getId() {
			return _levelId;
		}
	}

	/**
	 * 地域和id列表,可能一个地域名对应两个id
	 */
	private static Map<String, List<Integer>> _placeIdMap = new HashMap<String, List<Integer>>();

	/**
	 * 地域id和地域对象字典
	 */
	private static Map<Integer, Place> _placeMap = new HashMap<Integer, Place>();

	/**
	 * 座机区号和城市对应字典，区号包括0前缀
	 */
	private static Map<String, Place> _telPlaceMap = new HashMap<String, Place>();

	/**
	 * 手机前七位和城市对应字典
	 */
	private static Map<String, Place> _mobilePlaceMap = new HashMap<String, Place>();

	/**
	 * 直辖市(省级)地域id集合
	 */
	private static final Set<Integer> ZXCITYS = new HashSet<Integer>();

	private static void loadPlaceIdMapAndplaceMap() {
		InputStream in = null;
		try {

			int[] zxAreaArr = { 1, 2, 3, 33, 34, 35, 36 };
			for (int areaId : zxAreaArr) {
				ZXCITYS.add(areaId);
			}

			in = Place.class.getResourceAsStream("/area.txt");
			BufferedReader bw = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String line = null;
			while ((line = bw.readLine()) != null) {
				line = line.trim();
				String[] fields = StringUtils.split(line, '\t');
				if (fields.length != 4) {
					LOG.warn("area.txt line fields num not 4, num:"
							+ fields.length + ", line:" + line);
					continue;
				}
				int areaId = Integer.parseInt(fields[0]);
				String placeName = fields[1];
				int areaLevel = Integer.parseInt(fields[2]);
				int pareaId = Integer.parseInt(fields[3]);

				// 如果有多个地域名相同，因为在文本中顺序关系，保持了高级别地域排在前面
				if (_placeIdMap.containsKey(placeName)) {
					_placeIdMap.get(placeName).add(areaId);
				} else {
					List<Integer> areaIdList = new ArrayList<Integer>();
					areaIdList.add(areaId);
					_placeIdMap.put(placeName, areaIdList);
				}

				Place place = new Place();
				place.setPlaceId(areaId);
				place.setPlace(placeName);
				PlaceLevel pl;
				if (areaLevel == PlaceLevel.prov.getId()) {
					pl = PlaceLevel.prov;
				} else if (areaLevel == PlaceLevel.city.getId()) {
					pl = PlaceLevel.city;
				} else if (areaLevel == PlaceLevel.county.getId()) {
					pl = PlaceLevel.county;
				} else {
					LOG.warn("arealevel failed, arealevel: " + areaLevel);
					continue;
				}
				place.setLevel(pl);
				if (areaLevel != PlaceLevel.prov.getId()) {
					Place parent = _placeMap.get(pareaId);
					if (parent == null) {
						LOG.warn("parea is null, pareaId: " + parent);
						continue;
					}
					place.setParent(parent);
				}

				// 判断是否是直辖市
				if (ZXCITYS.contains(areaId)
						|| (place.getParent() != null && place.getParent().ZXCity)) {
					place.setZXCity(true);
				}

				_placeMap.put(areaId, place);
			}
			bw.close();

			if (_placeIdMap.size() == 0 || _placeMap.size() == 0) {
				LOG.error("_placeIdMap or _placeMap init failed");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			LOG.error("read area.txt failed", e);
			throw new RuntimeException();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e1) {
					LOG.error("close inputstream failed", e1);
					throw new RuntimeException();
				}
			}
		}
	}

	/**
	 * 加载座机区号和城市对应字典
	 */
	private static void loadTelPlaceMap() {

		InputStream in = null;
		String filename = "/phonecity.txt";
		try {
			in = Place.class.getResourceAsStream(filename);
			BufferedReader bw = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = bw.readLine()) != null) {
				String[] fields = line.trim().split("\\s");
				if (fields.length != 2) {
					LOG.warn(filename + " line fields num not 2");
					continue;
				}
				String areaCode = "0" + fields[0];
				int placeId = Integer.parseInt(fields[1]);
				Place place = _placeMap.get(placeId);
				if (place != null) {
					_telPlaceMap.put(areaCode, place);
				}
			}
			if (_telPlaceMap.size() == 0) {
				LOG.error("_telPlaceMap is empty");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			LOG.error("read " + filename + " failed", e);
			throw new RuntimeException();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e1) {
					LOG.error("close inputstream failed", e1);
					throw new RuntimeException();
				}
			}
		}
	}

	/**
	 * 加载手机前七位和城市对应字典
	 */
	private static void loadMobilePlaceMap() {

		InputStream in = null;
		String filename = "/mobilecity.txt";
		try {
			in = Place.class.getResourceAsStream(filename);
			BufferedReader bw = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = bw.readLine()) != null) {
				String[] fields = line.trim().split("\\s");
				if (fields.length != 2) {
					LOG.warn(filename + " line fields num not 2");
					continue;
				}
				String mobile = fields[0];
				String areaCode = "0" + fields[1];
				Place place = _telPlaceMap.get(areaCode);
				if (place != null) {
					_mobilePlaceMap.put(mobile, place);
				}
			}
			if (_mobilePlaceMap.size() == 0) {
				LOG.error("_mobilePlaceMap is empty");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			LOG.error("read " + filename + " failed", e);
			throw new RuntimeException();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e1) {
					LOG.error("close inputstream failed", e1);
					throw new RuntimeException();
				}
			}
		}
	}

	static {

		// 初始化，加载词典，数据较多，不便放在zookeeper上

		loadPlaceIdMapAndplaceMap();

		loadTelPlaceMap();

		loadMobilePlaceMap();
	}

	/**
	 * 根据place名查询对应id列表
	 * 
	 * @param placeName
	 * @return
	 */
	public static List<Integer> getPlaceIdList(String placeName) {
		if (StringUtils.isEmpty(placeName)) {
			return null;
		}

		return _placeIdMap.get(placeName);
	}

	/**
	 * 根据placeid查询Place对象
	 * 
	 * @param placeId
	 * @return
	 */
	public static Place getPlaceById(int placeId) {
		return _placeMap.get(placeId);
	}

	/**
	 * 根据区号查询Place对象，区号带0前缀
	 * 
	 * @param areaCode
	 * @return
	 */
	public static Place getPlaceByAreaCode(String areaCode) {
		if (StringUtils.isEmpty(areaCode)) {
			return null;
		}
		return _telPlaceMap.get(areaCode);
	}

	/**
	 * 根据手机号前7位查询Place对象
	 * 
	 * @param mobilePre
	 * @return
	 */
	public static Place getPlaceByMobile(String mobilePre) {
		if (StringUtils.isEmpty(mobilePre)) {
			return null;
		}
		return _mobilePlaceMap.get(mobilePre);
	}

	private int placeId = -1;
	/**
	 * 地域名
	 */
	private String place = "";
	/**
	 * 级别
	 */
	private PlaceLevel level = null;
	/**
	 * 父地域
	 */
	private Place parent = null;
	/**
	 * 是否是或者属于直辖市
	 */
	private boolean ZXCity = false;

	@Override
	public String toString() {
		return new StringBuilder().append("place:").append(place)
				.append(",id:").append(placeId).append(",level:").append(level)
				.toString();
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public PlaceLevel getLevel() {
		return level;
	}

	public void setLevel(PlaceLevel level) {
		this.level = level;
	}

	public Place getParent() {
		return parent;
	}

	public void setParent(Place parent) {
		this.parent = parent;
	}

	public boolean isZXCity() {
		return ZXCity;
	}

	public void setZXCity(boolean zXCity) {
		ZXCity = zXCity;
	}

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public static void main(String[] args) {
		System.out.println(getPlaceByMobile("1381131"));
		System.out.println(getPlaceByAreaCode("010"));
	}
}
