package crawler;
import java.net.*;
public interface ISpiderReportable {
  public boolean spiderFoundURL(int website, URL base,URL url);
  public void spiderURLError(URL url);
  public void spiderFoundEMail(String email);
  public void spiderTargetURL(int website, URL base,URL url);

}
