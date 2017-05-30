package demo.model;

/**
 * Information about an EC2 instance
 *
 * @author xma11 <maxiaohao@gmail.com>
 * @date 30 May 2017
 *
 */
public class InstInfo {

    String id;
    String name;
    String publicIp;
    String image;
    String type;
    String state;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPublicIp() {
        return publicIp;
    }


    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }


    public String getImage() {
        return image;
    }


    public void setImage(String image) {
        this.image = image;
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }

}
