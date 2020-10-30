package media;

import media.domain.CutInfo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
//        String inputPathname = "C:/Users/lilou/Desktop/video/制取氧气.mpg";
//        String inputPathname = "C:/Users/lilou/Desktop/video/龙猫.ts";

//        String inputPathname = "C:/Users/lilou/Desktop/video/忽然少年.ts";
        String inputPathname = "C:/Users/lilou/Desktop/video/卧虎藏龙.ts";

//        String inputPathname = "C:/Users/lilou/Desktop/video/驴得水.mp4";
//        String inputPathname = "C:/Users/lilou/Desktop/video/法证先锋Ⅲ 07.mp4";

//        String inputPathname = "C:/Users/lilou/Desktop/video/仙剑奇侠传三 01.mp4";

//        String inputPathname = "C:/Users/lilou/Desktop/video/香蜜沉沉烬如霜 01.mp4";
        testCutAndConcat(inputPathname, 3);
        testCutAndConcat(inputPathname, 5);
        testCutAndConcat(inputPathname, 10);
        testCutAndConcat(inputPathname, 15);
        testCutAndConcat(inputPathname, 20);
    }

    private static void testCutAndConcat(String inputPathname, int num) {

        MediaUtil.setFFmpegPath("D:/c/ffmpeg/bin/ffmpeg.exe");
        Time timeStart = Time.valueOf("00:00:10");
        Time timeEnd = Time.valueOf("00:00:20");
        int step = 20;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        TreeSet<CutInfo> cutInfos = new TreeSet<>();
        for (int i = 0; i < num; i++) {
            LocalTime start = timeStart.toLocalTime().plusSeconds(step * i);
            LocalTime end = timeEnd.toLocalTime().plusSeconds(step * i);
            cutInfos.add(new CutInfo(inputPathname, start.format(formatter), end.format(formatter)));
        }
        System.out.println(cutInfos);


        long t1 = System.currentTimeMillis();
        cut(cutInfos);
        long t2 = System.currentTimeMillis();

        List<String> collect = cutInfos.stream().map(CutInfo::getOutputPathname).collect(Collectors.toList());
        concat(collect);
        long t3 = System.currentTimeMillis();

        System.out.println("------------------------");
        System.out.println("system info: ");
        printSystemInfo();
        System.out.println("输入信息：" + getInputInfo(cutInfos));
        System.out.println("视频数量：" + collect.size());
        System.out.println("视频格式：" + getFormatInfo(cutInfos));
        System.out.println("剪切时间：" + (t2 - t1) + "ms");
        System.out.println("合并时间：" + (t3 - t2) + "ms");
        System.out.println("总时间：" + (t3 - t1) + "ms");
        System.out.println("------------------------");
    }

    private static String getFormatInfo(TreeSet<CutInfo> cutInfos) {
        if (cutInfos.isEmpty()) {
            return null;
        }
        String inputPathname = cutInfos.first().getInputPathname();
        int dotIndex = inputPathname.indexOf(".");
        return inputPathname.substring(dotIndex);
    }

    // https://stackoverflow.com/questions/25552/get-os-level-system-information/61727
    private static void printSystemInfo() {
        /* Total number of processors or cores available to the JVM */
        System.out.println("Available processors (cores): " +
                Runtime.getRuntime().availableProcessors());

        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " +
                Runtime.getRuntime().freeMemory());

        /* This will return Long.MAX_VALUE if there is no preset limit */
        long maxMemory = Runtime.getRuntime().maxMemory();
        /* Maximum amount of memory the JVM will attempt to use */
        System.out.println("Maximum memory (bytes): " +
                (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

        /* Total memory currently available to the JVM */
        System.out.println("Total memory available to JVM (bytes): " +
                Runtime.getRuntime().totalMemory());

    }

    private static String getInputInfo(Collection<CutInfo> cutInfos) {
        StringBuilder sb = new StringBuilder();
        for (CutInfo cutInfo : cutInfos) {
            sb
                    .append("\n\t")
                    .append(cutInfo.getInputPathname())
                    .append("\t\t")
                    .append(cutInfo.getStartTime())
                    .append("\t")
                    .append(cutInfo.getEndTime());
        }
        return sb.toString();
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
