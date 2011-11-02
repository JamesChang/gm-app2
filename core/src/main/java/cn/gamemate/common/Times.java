package cn.gamemate.common;

public class Times {
	public static String seconds2CN_short(int seconds){
		if (seconds < 60){
			return String.valueOf(seconds) + "秒";
		}
		seconds /= 60;
		return String.valueOf(seconds) + "分钟";
		//TODO: add more
	}
}
