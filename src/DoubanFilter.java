import java.net.URL;

class DoubanFilter {

	public static boolean URLFilter(URL url) {
		String host = url.getHost();
		String path = url.getPath();
		return (host.matches("\\w{1,}.douban.com") && 
				(path.equalsIgnoreCase("") || path.matches("/location") || path.matches("/location/.{0,}") ));
	}
	public static boolean activityFilter(URL url) {
		String path = url.getPath();
		return path.matches("/event/\\d{1,}/");
	}
}
