package twt.service.tianchi2;

import com.alibaba.tianchi.garbage_image_util.ImageClassSink;
import com.alibaba.tianchi.garbage_image_util.ImageDirSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;

public class RunMain {
    public static SavedModelBundle bundle;
    public static String[] classIndex = new String[100];
    public static Session sess;
    public static void main(String[] args) throws Exception {
        readIndex();
        loadModel();
        StreamExecutionEnvironment flinkEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        flinkEnv.setParallelism(1);
        ImageDirSource source = new ImageDirSource();
        flinkEnv.addSource(source).setParallelism(1)
                .flatMap(new MyFlatMap()).setParallelism(4)
                .addSink(new ImageClassSink()).setParallelism(1);
        flinkEnv.execute();

    }

    public static void loadModel() {
        String modelpath = System.getenv("IMAGE_MODEL_PATH");
        bundle = SavedModelBundle.load(modelpath, "serve");
        sess = bundle.session();
    }

    public static Session getSession() {
        return bundle.session();
    }
    public static void readIndex() {
//        FileReader reader = null;
//        try {
//            reader = new FileReader("src/main/java/class_index.txt");
//            BufferedReader br = new BufferedReader(reader);
//        String str;
//        while((str = br.readLine()) != null) {
           String str  = "奶粉 26\n" +
                   "纸箱 75\n" +
                   "胶水 79\n" +
                   "吹风机 16\n" +
                   "塑料玩具 20\n" +
                   "椅子 53\n" +
                   "充电器 7\n" +
                   "塑料袋 23\n" +
                   "纸尿裤 73\n" +
                   "牙刷 62\n" +
                   "剃须刀 11\n" +
                   "辣椒 90\n" +
                   "土豆 17\n" +
                   "瓶盖 66\n" +
                   "一次性塑料手套 1\n" +
                   "抹布 38\n" +
                   "杏核 49\n" +
                   "充电线 10\n" +
                   "塑料盖子 22\n" +
                   "干电池 28\n" +
                   "烟盒 61\n" +
                   "中性笔 4\n" +
                   "旧镜子 46\n" +
                   "充电宝 8\n" +
                   "鼠标 99\n" +
                   "水彩笔 55\n" +
                   "蒜皮 85\n" +
                   "旧玩偶 45\n" +
                   "退热贴 92\n" +
                   "废弃食用油 30\n" +
                   "青椒 96\n" +
                   "口服液瓶 15\n" +
                   "一次性纸杯 3\n" +
                   "纽扣 76\n" +
                   "指甲油瓶子 40\n" +
                   "插座 42\n" +
                   "充电电池 9\n" +
                   "塑料桶 19\n" +
                   "袜子 89\n" +
                   "电视机 67\n" +
                   "护手霜 35\n" +
                   "手表 32\n" +
                   "红豆 72\n" +
                   "衣架 88\n" +
                   "消毒液瓶 60\n" +
                   "医用棉签 14\n" +
                   "扫把 34\n" +
                   "海绵 59\n" +
                   "塑料包装 18\n" +
                   "菜刀 81\n" +
                   "蛋_蛋壳 87\n" +
                   "剪刀 12\n" +
                   "暖宝宝贴 47\n" +
                   "纸巾_卷纸_抽纸 74\n" +
                   "糖果 71\n" +
                   "铅笔屑 94\n" +
                   "头饰 25\n" +
                   "泡沫盒子 57\n" +
                   "打火机 33\n" +
                   "杀虫剂 48\n" +
                   "毛毯 54\n" +
                   "自行车 80\n" +
                   "耳机 77\n" +
                   "信封 6\n" +
                   "酸奶盒 93\n" +
                   "作业本 5\n" +
                   "拖把 39\n" +
                   "外卖餐盒 24\n" +
                   "水龙头 56\n" +
                   "旧帽子 44\n" +
                   "蒜头 84\n" +
                   "白糖_盐 69\n" +
                   "蚊香 86\n" +
                   "快递盒 31\n" +
                   "胶带 78\n" +
                   "菜板 82\n" +
                   "抱枕 37\n" +
                   "洗面奶瓶 58\n" +
                   "空调机 70\n" +
                   "废弃衣服 29\n" +
                   "面膜 97\n" +
                   "香烟 98\n" +
                   "无纺布手提袋 43\n" +
                   "PET塑料瓶 0\n" +
                   "姜 27\n" +
                   "护肤品玻璃罐 36\n" +
                   "过期化妆品 91\n" +
                   "陶瓷碗碟 95\n" +
                   "化妆品瓶 13\n" +
                   "棉签 52\n" +
                   "指甲钳 41\n" +
                   "牛奶盒 65\n" +
                   "牙签 63\n" +
                   "塑料盆 21\n" +
                   "葡萄干 83\n" +
                   "果皮 51\n" +
                   "牙膏皮 64\n" +
                   "一次性筷子 2\n" +
                   "电风扇 68\n" +
                   "杯子 50";
           String res[] = str.split("\n");
        for (int i = 0; i < res.length; i++) {
            String rr []= res[i].split(" ");
            classIndex[Integer.valueOf(rr[1])]=rr[0];
        }

            //System.out.println(str);
//        }
//
//        br.close();
//        reader.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

    }
}