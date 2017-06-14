package org.uavteam;

/**
 * Created by James on 6/13/2017.
 */

public class SubmitThread extends Thread {
    TargetData td;
    public SubmitThread(TargetData data){
        super();
        td=data;
    }
    @Override
    public void run() {
        ImageViewer.submitImage(td);

    }

}
