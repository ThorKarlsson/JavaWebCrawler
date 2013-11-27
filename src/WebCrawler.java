import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Name: Thorgeir Karlsson
 *
 * Simple webcrawler which crawls given URL and subsqequent URL's until at least 1000 URL's
 * have been crawled.
 */
public class WebCrawler {

    List<URL> crawledUrls;
    URLConnector connector;

    public WebCrawler(String url){
        try{
            connector = new URLConnector();
            crawledUrls = new ArrayList<URL>();
            URL suppliedUrl = new URL(url);
            beginCrawl(suppliedUrl);
        }
        catch(MalformedURLException ex){
            System.out.println("Invalid URL");
            System.out.println("Usage: WebCrawler <URL>");
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void beginCrawl(URL startUrl) throws IOException {
        URL target = startUrl;
        crawledUrls.add(startUrl);
        int iterator = 0;

        while(crawledUrls.size() < 1000 && iterator < crawledUrls.size()){
            crawledUrls.addAll(connector.GetAllURLs(target));
            target = crawledUrls.get(iterator);
            iterator++;
        }

        outputResult();
    }

    private void outputResult() {
        for(URL url : crawledUrls){
            System.out.println(url.toString());
        }
        System.out.println("\nWebCrawl Complete");
    }

    public static void main(String args[]) throws IOException {
        if(args.length == 0){
            System.out.println("Usage: WebCrawler <URL>");
            System.exit(0);
        }

        WebCrawler wc = new WebCrawler(args[0]);
    }
}
