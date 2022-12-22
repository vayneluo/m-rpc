package me.lattice.mrpc.example.curator;

import lombok.extern.slf4j.Slf4j;
import me.lattice.mrpc.example.constants.CuratorExampleConstant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.Arrays;

/**
 * @description:
 * @author: lattice
 * @date: 2022/12/22 9:00 AM
 */
@Slf4j
public class CuratorApiExample {

    public static void main(String[] args) throws Exception{

        //CuratorHelper.createNode(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/test", "test");
//        CuratorHelper.delete(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/test");
//        byte[] nodeData = CuratorHelper.getNodeData(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/test");
//        log.info(Arrays.toString(nodeData));

        //CuratorHelper.createNode(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/serviceA/instance01", "instance01");
        //CuratorHelper.createNode(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/serviceA/instance02", "instance02");
        //CuratorHelper.createNode(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/serviceA/instance03", "instance03");

        CuratorHelper.chlidrenList(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/serviceA")
                .forEach(System.out::println);

        //CuratorHelper.addListener(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/serviceA");
        CuratorHelper.watchedGetChildren(CuratorExampleConstant.CURATOR_EXAMPLE_PATH + "/serviceA",
                watchedEvent -> System.out.println("watchedEvent = " + watchedEvent));

        Thread.sleep(1000000);
        CuratorHelper.close();

    }
}
