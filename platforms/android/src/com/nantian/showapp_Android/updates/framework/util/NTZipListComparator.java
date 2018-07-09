package com.nantian.showapp_Android.updates.framework.util;

import java.util.Comparator;

import com.nantian.showapp_Android.updates.mfp.domain.ZipInfo;

public class NTZipListComparator implements Comparator<ZipInfo> {

	@Override
	public int compare(ZipInfo lhs, ZipInfo rhs) {
		return compareVersion(lhs.getZip_version(), rhs.getZip_version());
	}

	//版本比较方法，version1>version2返回1，version1==version2返回0，version1<version2返回-1，
	private int compareVersion(String version1, String version2)  {  
	    if (version1 == null || version2 == null) {  
	        //throw new Exception("compareVersion error:illegal params.");
	    	System.out.println("compareVersion error:illegal params.");
	    }  
	    String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；  
	    String[] versionArray2 = version2.split("\\.");  
	    int idx = 0;  
	    int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值  
	    int diff = 0;  
	    while (idx < minLength  
	            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度  
	            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符  
	        ++idx;  
	    }  
	    //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；  
	    diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;  
	    return diff;  
	} 
}
