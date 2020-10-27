package media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.Arrays;

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
        // cut(path);

        concat();


    }

    private static void concat() {
        long begin = System.currentTimeMillis();
        MediaUtil.setFFmpegPath("D:/c/ffmpeg/bin/ffmpeg.exe");
        try {
            MediaUtil.concatVideoList(Arrays.asList(
                    "C:/Users/lilou/Desktop/video/intermediate1.mpg",
                    "C:/Users/lilou/Desktop/video/aa.mp4"
//                    "C:/Users/lilou/Desktop/video/intermediate1.mpg",
            ), MediaUtil.ConcatType.DEMUXER, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        // 合并视频文件
//        MediaUtil.mergeVideo(Arrays.asList(
//                "C:/Users/lilou/Desktop/video/intermediate1.mpg",
//                "C:/Users/lilou/Desktop/video/intermediate2.mpg"
//        ));

        long end = System.currentTimeMillis();
        System.out.println(end - begin);
    }

    private static void cut() {
        String path = "C:/Users/lilou/Desktop/video/";
        MediaUtil.setFFmpegPath("D:/c/ffmpeg/bin/ffmpeg.exe");
        MediaUtil.cutVideo(
                new File(path, "input.mp4"),
                new File(path, "output2.mp4"),
                Time.valueOf("00:01:10"),
                Time.valueOf("00:01:25")
        );
    }
}
