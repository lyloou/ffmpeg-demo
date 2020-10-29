package media;

import media.domain.CutInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * <p>类的详细说明</p>
 *
 * @author lyloou
 * @author 其他作者姓名
 * @version 1.00 2020/10/26 , 星期一 lyloou 创建
 * <p>1.01 YYYY/MM/DD 修改者姓名 修改内容说明</p>
 */
@SpringBootApplication
public class FfmpegApplication {
    public static void main(String[] args) {
        SpringApplication.run(FfmpegApplication.class, args);
        cutAndConcat();
    }

    private static void cutAndConcat() {

        MediaUtil.setFFmpegPath("D:/c/ffmpeg/bin/ffmpeg.exe");
        TreeSet<CutInfo> cutInfos = new TreeSet<>(Arrays.asList(
                new CutInfo("C:/Users/lilou/Desktop/video/input.mp4", "00:01:10", "00:01:20"),
                new CutInfo("C:/Users/lilou/Desktop/video/input.mp4", "00:01:10", "00:01:25"),
                new CutInfo("C:/Users/lilou/Desktop/video/input.mp4", "00:00:10", "00:00:20"),
                new CutInfo("C:/Users/lilou/Desktop/video/input.mp4", "00:00:30", "00:00:40"),
                new CutInfo("C:/Users/lilou/Desktop/video/input.mp4", "00:00:50", "00:00:60")
        ));


        long t1 = System.currentTimeMillis();
        cut(cutInfos);
        long t2 = System.currentTimeMillis();
        System.out.println("cut time: " + (t2 - t1));

        List<String> collect = cutInfos.stream().map(CutInfo::getOutputPathname).collect(Collectors.toList());
        concat(collect);
        long t3 = System.currentTimeMillis();
        System.out.println("concat time:" + (t3 - t2));

        System.out.println("total time:" + (t3 - t1));
    }

    private static void concat(List<String> list) {
        try {
            MediaUtil.concatVideoList(list, MediaUtil.ConcatType.DEMUXER, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cut(Collection<CutInfo> cutInfos) {
        for (CutInfo cutInfo : cutInfos) {
            MediaUtil.cutVideo(
                    cutInfo.getInputFile(),
                    cutInfo.getOutputFile(),
                    Time.valueOf(cutInfo.getStartTime()),
                    Time.valueOf(cutInfo.getEndTime())
            );
        }
    }
}
