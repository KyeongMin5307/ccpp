package com.gachon.ccpp.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {
    private Document html;

    public HtmlParser(){}

    public HtmlParser(Document document){
        this.html = document;
    }

    public void setHtml(Document html) {
        this.html = html;
    }

    // 개인정보
    // Uri "user/user_edit.php"
    public ArrayList<String> getStudentInfo() {
        Elements selected = html.select( ".felement.fstatic");
        ArrayList<String> data = new ArrayList<>();

        for(Element e: selected)
            data.add(e.text());

        data.remove(2);
        data.remove(2);

        return data;
    }
<<<<<<< Updated upstream
=======

    // 달력
    // Uri "calendar/view.php?view=month"
    // 얘를 이용해서 링크를 얻어오고 링크마다 html을 받아와 다시 파싱해야함
    // date - 날짜
    public ArrayList<ListForm> getMonthList() {
        Elements selected = html.select( ".duration .day a");
        ArrayList<ListForm> data = new ArrayList<>();

        for(Element e: selected)
            data.add(new ListForm("", e.text(), "", e.attr("href"), ""));

        return data;
    }

    // title - 과제이름
    // writer - 강의이름
    // payload - 설명
    public ArrayList<ListForm> getDayList() {
        Elements selected = html.select( ".eventlist .event");
        ArrayList<ListForm> data = new ArrayList<>();

        for(Element e: selected) {
            String a = e.select("img").attr("title");

            if (a.compareTo("과제") != 0)
                continue;

            Elements temp = e.select(".description");
            temp.select("br").before("\\n");
            temp.select("p").before("\\n");

            String text = temp.html().replaceAll("\\\\n","\n");
            text = Jsoup.clean(text,"", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)).replaceAll("&nbsp;"," ");

            data.add(new ListForm(
                    e.select(".referer a").text(),
                    "",
                    e.select(".course a").text(),
                    e.select(".referer a").attr("href"),
                    text));
        }
        return data;
    }
>>>>>>> Stashed changes

    // 메인화면 강의리스트
    // Uri ""
    public ArrayList<String> getCourseLink() {
        Elements selected = html.select( ".course_lists .course_link");
        ArrayList<String> data = new ArrayList<>();

        for(Element e: selected)
            data.add(e.attr("href"));

        return data;
    }

    public ArrayList<String> getCourseTitle() {
        Elements selected = html.select( ".course_lists .course-title");
        ArrayList<String> data = new ArrayList<>();

        for(Element e: selected)
            data.add(e.select("h3").first().text());

        return data;
    }

    public ArrayList<String> getCourseProf() {
        Elements selected = html.select( ".course_lists .prof");
        ArrayList<String> data = new ArrayList<>();

        for(Element e: selected)
            data.add(e.text());
        return data;
    }

    // date,payload => "" 데이터없음
    // title => 강의이름
    // writer => 교수이름
    public ArrayList<ListForm> getCourseList() {
        Elements selected = html.select( ".course_lists .course_box");
        ArrayList<ListForm> data = new ArrayList<>();
<<<<<<< Updated upstream
        for (Element e: selected){
=======

        for (Element e : selected)
>>>>>>> Stashed changes
            data.add(new ListForm(
                    e.select(".course-title h3").text(),
                    "",
                    e.select(".prof").text(),
                    e.select(".course_link").attr("href"),
<<<<<<< Updated upstream
                    "",
                    e.select(".course-image img").attr("src")));
        }
=======
                    e.select(".course-image img").attr("src")));

>>>>>>> Stashed changes
        return data;
    }

    //강의 내에서 세부정보
    //Uri "course/view.php?id=xxxxx"
    public ArrayList<String> getInCourseTitle() {
        Elements selected = html.select( ".total_sections .mod-indent-outer .instancename");
        ArrayList<String> data = new ArrayList<>();

        for(Element e : selected)
            data.add(e.text());

        return data;
    }

    public ArrayList<String> getInCourseLink(){
        Elements selected = html.select( ".total_sections .mod-indent-outer a");
        ArrayList<String> data = new ArrayList<>();

        for(Element e : selected)
            data.add(e.attr("href"));
        return data;
    }

    //모든 주차의 내용을 긁어옴
    // CourseListForm
    //      announcement => 해당 주차 공지 -> 없는경우도 많음
    //      week => 주차
    //      ListForm.java
    //          date => file의 경우 용량/assignment의 경우 기간/Vod일 경우 기간, 영상길이
    //          writer => file/vod 구분 값이 없으면 assignment
    //          payload => 해당 항목 공지 -> 없는경우도 많음
    public ArrayList<CourseListFrom> getInCourseList() {
        Elements selected = html.select( ".total_sections .content");
        ArrayList<CourseListFrom> data = new ArrayList<>();
        ArrayList<ListForm> items;

        for (Element e : selected) {
            Elements item = e.select(".mod-indent-outer");
            items = new ArrayList<ListForm>();

            for (Element i : item)
                items.add(new ListForm(
                        i.select(".instancename").text(),
                        i.select(".displayoptions").text(),
                        i.select( ".accesshide").text(),
                        i.select("a").attr("href"),
<<<<<<< Updated upstream
                        i.select( ".contentafterlink").text(),
                        null));
            }
=======
                        i.select( ".contentafterlink").text()));
>>>>>>> Stashed changes
            data.add(new CourseListFrom(
                    e.select(".contentwithoutlink").text(),
                    e.select("h3").text(),
                    items));
        }
        return data;
    }

    //공지 관련
    //Uri ""
    //아래 함수들을 쓰기위한 강의의 공지사항링크를 가져옴
<<<<<<< Updated upstream
    public ArrayList<String> getClassAnnouncementLink(){
=======
    public String getClassAnnouncementLink() {
>>>>>>> Stashed changes
        Elements selected = html.select( "#section-0 .content a");
        ArrayList<String> data = new ArrayList<>();

        for(Element e: selected)
            data.add(e.attr("href"));
<<<<<<< Updated upstream
        }
        data.remove(1);
        return data;
=======

        return data.get(0);
>>>>>>> Stashed changes
    }

    //강의 내부 공지
    //uri mod/ubboard/view.php?id=xxxxxx + &ls=100
    public ArrayList<String> getAnnouncementsLink() {
        Elements selected = html.select(".ubboard_container .list a");
        ArrayList<String> data = new ArrayList<>();

        for(Element e : selected)
            data.add(e.attr("href"));

        return data;
    }

    //제목말고 기타 정보도 들어가있음
    public ArrayList<String> getCourseAnnouncementTitle() {
        Elements selected = html.select( ".ubboard_container .list td");
        ArrayList<String> data = new ArrayList<>();

        for(Element e : selected)
            data.add(e.text());

        return data;
    }

    // payload => 공지번호
    public ArrayList<ListForm> getCourseAnnouncementList() {
        Elements selected = html.select(".ubboard_container .list tr");
        ArrayList<ListForm> data = new ArrayList<>();

        for (Element e : selected) {
            Elements row = e.select("td");

            if (row.size()!=0)
                data.add(new ListForm(
                        row.get(1).text(),
                        row.get(3).text(),
                        row.get(2).text(),
                        row.get(1).select("a").attr("href"),
<<<<<<< Updated upstream
                        row.get(0).text(),
                        null));
            }
=======
                        row.get(0).text()));
>>>>>>> Stashed changes
        }

        return data;
    }

    //공지 내용
    //uri mod/ubboard/article.php?id=xxxxxx&bwid=xxxxxx
    //payload => 조회수 -> ": XXX"
    public ContentForm getAnnouncementContent() {
        Elements selected = html.select( ".ubboard");
        selected.select(".title").empty();
<<<<<<< Updated upstream
        ContentForm data = new ContentForm(
=======

        return new ContentForm(
>>>>>>> Stashed changes
                selected.select(".subject").text(),
                selected.select(".info .date").text(),
                selected.select(".info .writer").text(),
                selected.select(".text_to_html").text(),
                selected.select(".info .hit").text());
    }

    // payload => "Next:" 또는 "Prev:"
    public ArrayList<ListForm> getPreNextAnnouncementList() {
        Elements selected = html.select( ".pre_next");
        ArrayList<ListForm> data = new ArrayList<>();
        for (Element e : selected)
            data.add(new ListForm(
                    e.select("a").text(),
                    e.select(".date").text(),
                    e.select(".writer").text(),
                    e.select("a").attr("href"),
<<<<<<< Updated upstream
                    e.select(".preface").text(),
                    null));
        }
=======
                    e.select(".preface").text()));

>>>>>>> Stashed changes
        return data;
    }

    // 전체공지
    // uri /local/ubnotification
    // 모든 강의 알람이 모여있기 때문에 데이터가 좀 다름
    // writer => 강의명
    // title => 새 XX이 등록되었습니다.
    // data => 몇시간전/며칠전
    // payload => X주차
    public ArrayList<ListForm> getAllAnnouncement() {
        Elements selected = html.select( ".media");
        ArrayList<ListForm> data = new ArrayList<>();
        for (Element e : selected) {
            String sectionname = e.select(".sectionname").text();
            String title = e.select("h4").text();
            e.select(".sectionname").empty();
            data.add(new ListForm(
                    e.select( "p").remove(1).text(),
                    e.select(".timeago").text(),
                    title.substring(0,title.length() - 2),
                    e.select("a").attr("href"),
                    sectionname,
                    null));
        }
        return data;
    }

    public ContentForm getAssignmentContent() {
        Elements selected = html.select(".generaltable.table.table-bordered .tr");

        String content = "";
        String link = "";

        Elements file = selected.get(6).select("ygtv1.ygtvitem");
        if (file.first() != null) {
            content = file.select("div a").text();
            link = file.select("div a").attr("href");
        }

        return new ContentForm(
                "",
                selected.get(3).child(1).text(),
                "",
                content,
                link);
    }

}