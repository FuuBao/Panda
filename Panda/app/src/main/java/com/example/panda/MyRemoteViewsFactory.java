package com.example.panda;

        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.widget.Adapter;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.RemoteViews;
        import android.widget.RemoteViewsService;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
        import java.util.List;

/**
 * 런처 앱에 리스트뷰의 어뎁터 역할을 해주는 클래스
 */
public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private int count;

    //context 설정하기
    public Context context;
    public ArrayList<WidgetItem> arrayList;
    public FirebaseDatabase database;
    public DatabaseReference myRef;
    private RecyclerAdapter recyclerAdapter;
    public MyRemoteViewsFactory(Context context) {
        this.context = context;
    }
    private RecyclerView recyclerView;
    private ArrayAdapter<WidgetItem> adapter;
    public DataSnapshot snapshot;

    String Id = ((LoginActivity)LoginActivity.context_main).Id;


    //DB를 대신하여 arrayList에 데이터를 추가하는 함수ㅋㅋ
    public void setData() {


        /*arrayList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("widgetItem");

        Memo memo = databaseReference.getDatabase();
        WidgetItem widgetItem=new WidgetItem(memo.getTitle());
        arrayList.add(widgetItem);*/



        arrayList = new ArrayList<>();// widgetitem 객체를 담을 어레이 리스트(어댑터쪽으로)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("WidgetItem");//db테이블 연결
        databaseReference = database.getReference();
        databaseReference.child(Id).child("memo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();//기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 List를 추출
                    Memo memo = snapshot.getValue(Memo.class); //widgetite
                    WidgetItem widgetItem=new WidgetItem(memo.getTitle());
                    arrayList.add(widgetItem);
                    //arrayList.add(new WidgetItem("memo.getTitle()"));
                }
                //recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("MemoActivity", String.valueOf(databaseError.toException()));

            }
        });

        //adapter = new ArrayAdapter<WidgetItem>(this.context, android.R.layout.list_item, arrayList);
        //recyclerView.setAdapter(recyclerAdapter);


        /*myRef = database.getReference().child(Id); // 로그인 시 표시되는 recyclerview - db에서 불러오기
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //파이어베이스 테이터베이스의 데이터를 받아오는 곳
                recyclerAdapter.clear();//기존 배열 리스트가 존재하지 않게 초기화

                for (DataSnapshot memoData : dataSnapshot.getChildren()) {
                    //String t=memoData.
                    Memo getMemo = memoData.getValue(Memo.class);
                    WidgetItem wi=new WidgetItem(getMemo.getTitle());
                    //adpater 달기

                    
                    // child 내에 있는 데이터만큼 반복합니다.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        //arrayList = FirebaseDatabase.getInstance(context).getApp();

        //arrayList = new ArrayList<>();
        //arrayList.add(new WidgetItem(1, "First"));
        //arrayList.add(new WidgetItem(2, "Second"));
        //arrayList.add(new WidgetItem(3, "Third"));
        //arrayList.add(new WidgetItem(4, "Fourth"));
        //arrayList.add(new WidgetItem(5, "Fifth"));
    }


    //이 모든게 필수 오버라이드 메소드

    //실행 최초로 호출되는 함수
    @Override
    public void onCreate() {
        setData();
    }

    //항목 추가 및 제거 등 데이터 변경이 발생했을 때 호출되는 함수
    //브로드캐스트 리시버에서 notifyAppWidgetViewDataChanged()가 호출 될 때 자동 호출
    @Override
    public void onDataSetChanged() {
        setData();
    }

    //마지막에 호출되는 함수
    @Override
    public void onDestroy() {

    }

    // 항목 개수를 반환하는 함수
    @Override
    public int getCount() {
        return arrayList.size();
    }

    //각 항목을 구현하기 위해 호출, 매개변수 값을 참조하여 각 항목을 구성하기위한 로직이 담긴다.
    // 항목 선택 이벤트 발생 시 인텐트에 담겨야 할 항목 데이터를 추가해주어야 하는 함수
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews listviewWidget = new RemoteViews(context.getPackageName(), R.layout.item_collection);
        listviewWidget.setTextViewText(R.id.text1, arrayList.get(position).getContent());

        // 항목 선택 이벤트 발생 시 인텐트에 담겨야 할 항목 데이터를 추가해주는 코드
        Intent dataIntent = new Intent();
        dataIntent.putExtra("content", arrayList.get(position).getContent());
        listviewWidget.setOnClickFillInIntent(R.id.text1, dataIntent);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(ListViewWidget.ACTION_TOAST, position);
        listviewWidget.setOnClickFillInIntent(R.id.widget_listview, fillInIntent);


        return listviewWidget;
    }

    //로딩 뷰를 표현하기 위해 호출, 없으면 null
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    //항목의 타입 갯수를 판단하기 위해 호출, 모든 항목이 같은 뷰 타입이라면 1을 반환하면 된다.
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    //각 항목의 식별자 값을 얻기 위해 호출
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 같은 ID가 항상 같은 개체를 참조하면 true 반환하는 함수
    @Override
    public boolean hasStableIds() {
        return false;
    }
}
