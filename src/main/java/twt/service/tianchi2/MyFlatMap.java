package twt.service.tianchi2;

import com.alibaba.tianchi.garbage_image_util.IdLabel;
import com.alibaba.tianchi.garbage_image_util.ImageData;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import org.tensorflow.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.io.*;

public class MyFlatMap implements FlatMapFunction<ImageData, IdLabel> {
    public MyFlatMap() {
    }


    public void flatMap(ImageData value, Collector<IdLabel> out) throws Exception {
        IdLabel idLabel = new IdLabel();
        if(value.getId()!=null){
        idLabel.setId(value.getId());
        }else{
            System.out.println("null");
            idLabel.setId(String.valueOf(System.currentTimeMillis()));
        }
        String modelpath = System.getenv("IMAGE_MODEL_PATH");
//        System.out.println(modelpath + "/saved_model.pb");
//        try (SavedModelBundle bundle = SavedModelBundle.load(modelpath, "serve")) {
//            graph.importGraphDef(Files.readAllBytes(Paths.get(
//                    modelpath+"/saved_model.pb"
//            )));
//            byte[] graphBytes = IOUtils.toByteArray(new FileInputStream(modelpath+"/saved_model.pb"));
//            graph.importGraphDef(graphBytes);

//            graph
            try (Session sess = RunMain.sess) {
                ByteArrayInputStream in = new ByteArrayInputStream(value.getImage());
                BufferedImage image = ImageIO.read(in);

                ComponentColorModel colorModel = (ComponentColorModel) image.getColorModel();
//                byte r[],g[],b[];
//                colorModel.getReds(r);
//                colorModel.getGreens(g);
//                colorModel.getBlues(b);
                int width = image.getWidth() > 224 ? 224 : image.getWidth();
                int height = image.getHeight() > 224 ? 224 : image.getHeight();

                float[][][][] input = new float[1][224][224][3];
                for (int i = 0; i < width; ++i) {
                    for (int j = 0; j < height; ++j) {
                        Object data = image.getRaster().getDataElements(i, j, null);
                        input[0][i][j][0] = colorModel.getRed(data);
                        input[0][i][j][1] = colorModel.getGreen(data);
                        input[0][i][j][2] = colorModel.getBlue(data);
                    }
                }
               //Iterator<Operation> it =bundle.graph().operations();
//                while (it.hasNext()){
//
//                    System.out.println(it.next().name());
//                    it.next();
//                }

                try (Tensor x = Tensor.create(input);
                     // input是输入的name，output是输出的name
                     Tensor y = sess.runner().feed("input_1", x).fetch("fc100/Softmax").run().get(0)
                ) {
                    float[][] result = new float[1][100];
                    y.copyTo(result);
                    float max = 0;
                    int index = 0;
                    for (int i = 0; i < 100; i++) {
                        if(result[0][i]>max){
                            max = result[0][i];
                            index = i;
                      }
                        //System.out.println(String.valueOf(result[0][i]));
                    }
//                    System.out.println(RunMain.classIndex[index]);
//                    System.out.println("res"+index);
                    if(RunMain.classIndex[index]!=null){
                        idLabel.setLabel(RunMain.classIndex[index]);
                    }else{
                        idLabel.setLabel(String.valueOf(System.currentTimeMillis()));
                        System.out.println("null");
                    }
                    out.collect(idLabel);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("gg");
            }
//        }

       // System.out.println("ok");

    }


}

