package tool;

import org.rendersnake.HtmlCanvas;
import org.rendersnake.Renderable;

import java.io.IOException;

public class TurnToHtml implements Renderable{

    @Override
    public void renderOn(HtmlCanvas htmlCanvas) throws IOException {

    }

    public static String turn(String content){
        HtmlCanvas html = new HtmlCanvas();
        try {
            html
                    .html()
                    .head()
                    ._head()
                    .body()
                    .h2().content(content)
                    ._body()
                    ._html();

            return html.toHtml();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
