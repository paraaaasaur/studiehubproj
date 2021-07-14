package com.group5.springboot.utils;

import org.springframework.stereotype.Component;

@Component
//隨機產生一組密碼
public class GenerateRandomPassword {

	public static String generatePasswordProcess(){
		String alphabets = "abcdefghijklmnopqrstuvwxyz";
		int[] nums = new int[] {0,1,2,3,4,5,6,7,8,9};
		char[] alphabetsArray = alphabets.toCharArray();
		String randomPassword = randomSelection(alphabetsArray, nums);
		System.out.println(randomPassword); 	//測試
		return randomPassword;
	}

	public static String randomSelection(char[] apbArray, int[] nums) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i<6 ; i++) {
			int rdNum = (int)(Math.random()*10);
			if(sb.indexOf(String.valueOf(apbArray[rdNum])) == -1) {
				sb.append(String.valueOf(apbArray[rdNum]));
				sb.append(String.valueOf(nums[rdNum]));
			}else {
				i--;
			}
		}
		
		return sb.toString();
	}

}
