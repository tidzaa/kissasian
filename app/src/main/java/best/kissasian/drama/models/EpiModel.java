package best.kissasian.drama.models;

import java.util.ArrayList;
import java.util.List;

public class EpiModel {
    String seson;
    String epi;
    String epiid;
    String streamURL;
    String serverType;
    String serverlabel;
    String titel;
    String linkdownload;
    String localurl;

    public String getLocalurl() {
        return localurl;
    }

    public void setLocalurl(String localurl) {
        this.localurl = localurl;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getServerlabel() {
        return serverlabel;
    }

    public void setServerlabel(String serverlabel) {
        this.serverlabel = serverlabel;
    }

    public List<SubtitleModel> listSubepi= new ArrayList<>();

    public List<SubtitleModel> getListSubepi() {
        return listSubepi;
    }

    public void setListSubepi(List<SubtitleModel> listSubepi) {
        this.listSubepi = listSubepi;
    }

    public String getEpiid() {
        return epiid;
    }

    public void setEpiid(String epiid) {
        this.epiid = epiid;
    }

    public String getLinkdownload() {
        return linkdownload;
    }

    public void setLinkdownload(String linkdownload) {
        this.linkdownload = linkdownload;
    }

    public String subtitleURLtv;

    public String getSubtitleURLtv() {
        return subtitleURLtv;
    }

    public void setSubtitleURLtv(String subtitleURLtv) {
        this.subtitleURLtv = subtitleURLtv;
    }

    public List<SubtitleModel> getListsubtv(int position) {
        return listsubtv;
    }

    public void setListsubtv(List<SubtitleModel> listsubtv) {
        this.listsubtv = listsubtv;
    }

    public List<SubtitleModel> listsubtv = new ArrayList<>();

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public void setStreamURL(String streamURL) {
        this.streamURL = streamURL;
    }

    public String getSeson() {
        return seson;
    }

    public void setSeson(String seson) {
        this.seson = seson;
    }

    public String getEpi() {
        return epi;
    }

    public void setEpi(String epi) {
        this.epi = epi;
    }
}
