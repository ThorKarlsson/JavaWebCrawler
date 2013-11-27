import sun.net.util.URLUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * Name: Thorgeir Karlsson
 */
public class URLConnector {

    Pattern anchorTagPattern = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
    Pattern hrefPattern = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");

    public List<URL> GetAllURLs(URL url) {
        List<URL> result = new ArrayList<URL>();

        String html = ExtractHtml(url);
        result.addAll(ExtractURLs(html));

        return result;
    }

    public String ExtractHtml(URL url) {
        try {
            BufferedReader output = RetrieveHtmlInputStream(url);
            String result = ReadHtml(output);

            return result;
        } catch (Exception ex) {
            //System.out.println(ex.getMessage());
        }

        return "";
    }

    private BufferedReader RetrieveHtmlInputStream(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        return new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
    }

    private String ReadHtml(BufferedReader output) throws IOException {
        String line;
        String result = "";
        while ((line = output.readLine()) != null) {
            result += line;
        }
        return result;
    }

    public List<URL> ExtractURLs(String html) {
        List<URL> result = new ArrayList<URL>();

        Matcher anchorMatcher = anchorTagPattern.matcher(html);

        while(anchorMatcher.find()){

            Matcher hrefMatcher = hrefPattern.matcher(anchorMatcher.group(1));

            while (hrefMatcher.find()){
                String link = hrefMatcher.group(1);
                if(link.contains("http"))
                {
                    URL temp = GetUrlForLink(link);
                    if(temp != null){
                        result.add(temp);
                    }
                }
            }
        }

        return result;
    }

    private URL GetUrlForLink(String link){
        try {
            link = RemoveInvalidCharacters(link);
            return new URL(link);
        } catch (Exception e) {
            return null;
        }
    }

    private String RemoveInvalidCharacters(String link) {
        link = link.replace("'", "");
        link = link.replace("\"", "");
        if(link.startsWith("http")){
            return link;
        }
        else{
            return null;
        }
    }
}
