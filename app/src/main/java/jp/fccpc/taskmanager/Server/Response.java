package jp.fccpc.taskmanager.Server;

/**
 * Created by hskk1120551 on 15/11/20.
 */
public class Response {
    public String bodyJSON;
    public String ETag;

    public Response(String bodyJSON, String ETag) {
        this.bodyJSON = bodyJSON;
        this.ETag = ETag;
    }

    @Override
    public String toString() {
        return "body: " + this.bodyJSON + ", ETag: " + this.ETag;
    }
}
