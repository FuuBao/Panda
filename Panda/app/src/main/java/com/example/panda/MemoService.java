package com.example.panda;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

/**
 * RemoteViewsService를 상속받은 개발자 서비스 클래스
 * RemoteViesFactory를 얻을 목적으로 인텐트 발생에 의해 실행됩니다.
 */
class MemoRemoteViewsService extends RemoteViewsService {

    //필수 오버라이드 함수 : RemoteViewsFactory를 반환한다.
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MemoRemoteViewsFactory(this.getApplicationContext());
    }
}

/*public class MemoService extends Service {
    public MemoService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}*/