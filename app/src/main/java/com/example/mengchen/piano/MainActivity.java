package com.example.mengchen.piano;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mengchen.piano.utils.RecordInfo;
import com.example.mengchen.piano.utils.RecordInfoUtils;
import com.example.mengchen.piano.utils.SysApplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class MainActivity extends Activity {

    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;

    private Map<Integer, Integer> tunesSoundMap = new HashMap<Integer, Integer>();
    private Map<Integer, Integer> tunesDiffMap = new HashMap<Integer, Integer>();
    private SoundPool soundPool;

    // 当前升调度数
    private int tunesAdd = 0;

    // 上次点击返回键的时间戳
    private long lastBackTimestamp = 0;

    // 是否正在录音
    private boolean isRecording = false;

    // 录音起始时间戳
    private long recordStartTime = 0;

    // 是否正在播放
    private boolean isPlaying = false;

    // 播放录音的定时器
    private Timer playTimer = null;

    // 录音时可录制的最小间隔时间（毫秒）
    public static final int RECORD_INTERVAL = 25;

    // 音调切换时，存储到录音数据的最小值 (用于区分按键音)
    private static final int TUNE_VAL_STARTED = 1000;

    // key: 时间戳  value: 音调值
    private Map<Long, List<RecordInfo>> timeTunesMap = new HashMap<>();

    @Override
    protected void onStart() {
        super.onStart();

        RadioGroup tunes = (RadioGroup) this.findViewById(R.id.tunes);

        final RadioButton tC = (RadioButton) this.findViewById(R.id.t0_btn);
        final RadioButton tCp = (RadioButton) this.findViewById(R.id.t1_btn);
        final RadioButton tD = (RadioButton) this.findViewById(R.id.t2_btn);
        final RadioButton tDp = (RadioButton) this.findViewById(R.id.t3_btn);
        final RadioButton tE = (RadioButton) this.findViewById(R.id.t4_btn);
        final RadioButton tF = (RadioButton) this.findViewById(R.id.t5_btn);
        final RadioButton tFp = (RadioButton) this.findViewById(R.id.t6_btn);
        final RadioButton tG = (RadioButton) this.findViewById(R.id.t7_btn);
        final RadioButton tGp = (RadioButton) this.findViewById(R.id.t8_btn);
        final RadioButton tA = (RadioButton) this.findViewById(R.id.t9_btn);
        final RadioButton tAp = (RadioButton) this.findViewById(R.id.t10_btn);
        final RadioButton tB = (RadioButton) this.findViewById(R.id.t11_btn);

        tunesDiffMap.put(tC.getId(), 0);
        tunesDiffMap.put(tCp.getId(), 1);
        tunesDiffMap.put(tD.getId(), 2);
        tunesDiffMap.put(tDp.getId(), 3);
        tunesDiffMap.put(tE.getId(), 4);
        tunesDiffMap.put(tF.getId(), 5);
        tunesDiffMap.put(tFp.getId(), 6);
        tunesDiffMap.put(tG.getId(), 7);
        tunesDiffMap.put(tGp.getId(), 8);
        tunesDiffMap.put(tA.getId(), 9);
        tunesDiffMap.put(tAp.getId(), 10);
        tunesDiffMap.put(tB.getId(), 11);

        tunes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                tunesAdd = tunesDiffMap.get(i);
                if (isRecording) {
                    long timestamp = (System.currentTimeMillis() - recordStartTime) / RECORD_INTERVAL;
                    RecordInfo recordInfo = new RecordInfo(TUNE_VAL_STARTED + tunesAdd, null);
                    List<RecordInfo> recordInfoList = new ArrayList<>();
                    recordInfoList.add(recordInfo);
                    timeTunesMap.put(timestamp, recordInfoList);
                }
            }
        });


        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        final Context context = MainActivity.this;

        /**
         * 将声音资源异步加载，不阻塞UI线程
         */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final int s34 = soundPool.load(context, R.raw.s34, 1);     // 5
                final int s35 = soundPool.load(context, R.raw.s35, 1);     // 5
                final int s36 = soundPool.load(context, R.raw.s36, 1);
                final int s37 = soundPool.load(context, R.raw.s37, 1);
                final int s38 = soundPool.load(context, R.raw.s38, 1);
                final int s39 = soundPool.load(context, R.raw.s39, 1);

                final int s40 = soundPool.load(context, R.raw.s40, 1);     // 1
                final int s41 = soundPool.load(context, R.raw.s41, 1);
                final int s42 = soundPool.load(context, R.raw.s42, 1);
                final int s43 = soundPool.load(context, R.raw.s43, 1);
                final int s44 = soundPool.load(context, R.raw.s44, 1);
                final int s45 = soundPool.load(context, R.raw.s45, 1);
                final int s46 = soundPool.load(context, R.raw.s46, 1);
                final int s47 = soundPool.load(context, R.raw.s47, 1);
                final int s48 = soundPool.load(context, R.raw.s48, 1);
                final int s49 = soundPool.load(context, R.raw.s49, 1);
                final int s50 = soundPool.load(context, R.raw.s50, 1);
                final int s51 = soundPool.load(context, R.raw.s51, 1);

                final int s52 = soundPool.load(context, R.raw.s52, 1);     // 1
                final int s53 = soundPool.load(context, R.raw.s53, 1);
                final int s54 = soundPool.load(context, R.raw.s54, 1);     // 2
                final int s55 = soundPool.load(context, R.raw.s55, 1);
                final int s56 = soundPool.load(context, R.raw.s56, 1);     // 3
                final int s57 = soundPool.load(context, R.raw.s57, 1);     // 4
                final int s58 = soundPool.load(context, R.raw.s58, 1);
                final int s59 = soundPool.load(context, R.raw.s59, 1);     // 5

                final int s60 = soundPool.load(context, R.raw.s60, 1);
                final int s61 = soundPool.load(context, R.raw.s61, 1);
                final int s62 = soundPool.load(context, R.raw.s62, 1);
                final int s63 = soundPool.load(context, R.raw.s63, 1);
                final int s64 = soundPool.load(context, R.raw.s64, 1);
                final int s65 = soundPool.load(context, R.raw.s65, 1);
                final int s66 = soundPool.load(context, R.raw.s66, 1);
                final int s67 = soundPool.load(context, R.raw.s67, 1);
                final int s68 = soundPool.load(context, R.raw.s68, 1);
                final int s69 = soundPool.load(context, R.raw.s69, 1);
                final int s70 = soundPool.load(context, R.raw.s70, 1);
                final int s71 = soundPool.load(context, R.raw.s71, 1);
                final int s72 = soundPool.load(context, R.raw.s72, 1);
                final int s73 = soundPool.load(context, R.raw.s73, 1);


                tunesSoundMap.put(34, s34);
                tunesSoundMap.put(35, s35);
                tunesSoundMap.put(36, s36);
                tunesSoundMap.put(37, s37);
                tunesSoundMap.put(38, s38);
                tunesSoundMap.put(39, s39);

                tunesSoundMap.put(40, s40);
                tunesSoundMap.put(41, s41);
                tunesSoundMap.put(42, s42);
                tunesSoundMap.put(43, s43);
                tunesSoundMap.put(44, s44);
                tunesSoundMap.put(45, s45);
                tunesSoundMap.put(46, s46);
                tunesSoundMap.put(47, s47);
                tunesSoundMap.put(48, s48);
                tunesSoundMap.put(49, s49);
                tunesSoundMap.put(50, s50);
                tunesSoundMap.put(51, s51);

                tunesSoundMap.put(52, s52);
                tunesSoundMap.put(53, s53);
                tunesSoundMap.put(54, s54);
                tunesSoundMap.put(55, s55);
                tunesSoundMap.put(56, s56);
                tunesSoundMap.put(57, s57);
                tunesSoundMap.put(58, s58);
                tunesSoundMap.put(59, s59);

                tunesSoundMap.put(60, s60);
                tunesSoundMap.put(61, s61);
                tunesSoundMap.put(62, s62);
                tunesSoundMap.put(63, s63);
                tunesSoundMap.put(64, s64);
                tunesSoundMap.put(65, s65);
                tunesSoundMap.put(66, s66);
                tunesSoundMap.put(67, s67);
                tunesSoundMap.put(68, s68);
                tunesSoundMap.put(69, s69);
                tunesSoundMap.put(70, s70);
                tunesSoundMap.put(71, s71);
                tunesSoundMap.put(72, s72);
                tunesSoundMap.put(73, s73);
            }
        }, 100);



        ImageView s34Btn = (ImageView) this.findViewById(R.id.s34_btn);
        ImageView s35Btn = (ImageView) this.findViewById(R.id.s35_btn);
        ImageView s36Btn = (ImageView) this.findViewById(R.id.s36_btn);
        ImageView s37Btn = (ImageView) this.findViewById(R.id.s37_btn);
        ImageView s38Btn = (ImageView) this.findViewById(R.id.s38_btn);
        ImageView s39Btn = (ImageView) this.findViewById(R.id.s39_btn);
        ImageView s40Btn = (ImageView) this.findViewById(R.id.s40_btn);
        ImageView s41Btn = (ImageView) this.findViewById(R.id.s41_btn);
        ImageView s42Btn = (ImageView) this.findViewById(R.id.s42_btn);
        ImageView s43Btn = (ImageView) this.findViewById(R.id.s43_btn);
        ImageView s44Btn = (ImageView) this.findViewById(R.id.s44_btn);
        ImageView s45Btn = (ImageView) this.findViewById(R.id.s45_btn);
        ImageView s46Btn = (ImageView) this.findViewById(R.id.s46_btn);
        ImageView s47Btn = (ImageView) this.findViewById(R.id.s47_btn);
        ImageView s48Btn = (ImageView) this.findViewById(R.id.s48_btn);
        ImageView s49Btn = (ImageView) this.findViewById(R.id.s49_btn);
        ImageView s50Btn = (ImageView) this.findViewById(R.id.s50_btn);
        ImageView s51Btn = (ImageView) this.findViewById(R.id.s51_btn);
        ImageView s52Btn = (ImageView) this.findViewById(R.id.s52_btn);
        ImageView s53Btn = (ImageView) this.findViewById(R.id.s53_btn);
        ImageView s54Btn = (ImageView) this.findViewById(R.id.s54_btn);
        ImageView s55Btn = (ImageView) this.findViewById(R.id.s55_btn);
        ImageView s56Btn = (ImageView) this.findViewById(R.id.s56_btn);
        ImageView s57Btn = (ImageView) this.findViewById(R.id.s57_btn);
        ImageView s58Btn = (ImageView) this.findViewById(R.id.s58_btn);
        ImageView s59Btn = (ImageView) this.findViewById(R.id.s59_btn);
        ImageView s60Btn = (ImageView) this.findViewById(R.id.s60_btn);
        ImageView s61Btn = (ImageView) this.findViewById(R.id.s61_btn);
        ImageView s62Btn = (ImageView) this.findViewById(R.id.s62_btn);

        s34Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(34, motionEvent, view);
            }
        });
        s35Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(35, motionEvent, view);
            }
        });
        s36Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(36, motionEvent, view);
            }
        });
        s37Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(37, motionEvent, view);
            }
        });
        s38Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(38, motionEvent, view);
            }
        });
        s39Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(39, motionEvent, view);
            }
        });
        s40Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(40, motionEvent, view);
            }
        });
        s41Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(41, motionEvent, view);
            }
        });
        s42Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(42, motionEvent, view);
            }
        });
        s43Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(43, motionEvent, view);
            }
        });
        s44Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(44, motionEvent, view);
            }
        });
        s45Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(45, motionEvent, view);
            }
        });
        s46Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(46, motionEvent, view);
            }
        });
        s47Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(47, motionEvent, view);
            }
        });
        s48Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(48, motionEvent, view);
            }
        });
        s49Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(49, motionEvent, view);
            }
        });
        s50Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(50, motionEvent, view);
            }
        });
        s51Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(51, motionEvent, view);
            }
        });
        s52Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(52, motionEvent, view);
            }
        });
        s53Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(53, motionEvent, view);
            }
        });
        s54Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(54, motionEvent, view);
            }
        });
        s55Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(55, motionEvent, view);
            }
        });
        s56Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(56, motionEvent, view);
            }
        });
        s57Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(57, motionEvent, view);
            }
        });
        s58Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(58, motionEvent, view);
            }
        });
        s59Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(59, motionEvent, view);
            }
        });
        s60Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(60, motionEvent, view);
            }
        });
        s61Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(61, motionEvent, view);
            }
        });
        s62Btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return playSound(62, motionEvent, view);
            }
        });

        // 录音按钮
        final Button recordBtn = (Button) this.findViewById(R.id.record_btn);
        recordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    isRecording = false;
                    recordBtn.setText("录音");

                    // 录音结束时存储一个空的RecordInfo，用于标识结束
                    // 如果没有任何按键，则不存储该标识
                    if (timeTunesMap.isEmpty()) return;

                    long timestamp = (System.currentTimeMillis() - recordStartTime) / RECORD_INTERVAL;
                    timeTunesMap.put(timestamp, Arrays.asList(new RecordInfo[0]));

                } else {
                    isRecording = true;
                    recordStartTime = System.currentTimeMillis();
                    timeTunesMap.clear();
                    recordBtn.setText("停止录音");
                }
            }
        });

        // 播放按钮
        final Button playBtn = (Button) this.findViewById(R.id.play_btn);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    final ImageView btn;

                    if (msg.obj != null) {
                        btn = (ImageView) msg.obj;
                    } else {
                        int tunes = msg.arg1;
                        btn = (ImageView) findViewById(getResources().getIdentifier("s" + tunes + "_btn", "id", "com.example.mengchen.piano"));
                    }

                    if ("B".equals(btn.getTag())) {
                        btn.setBackground(getResources().getDrawable(R.drawable.black_key_pressed));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn.setBackground(getResources().getDrawable(R.drawable.black_key_style));
                            }
                        }, 300);
                    } else {
                        btn.setBackground(getResources().getDrawable(R.drawable.white_key_pressed));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn.setBackground(getResources().getDrawable(R.drawable.white_key_style));
                            }
                        }, 300);
                    }

                } else if (msg.what == 2) {
                    isPlaying = false;
                    playBtn.setText("播放");
                    Toast.makeText(MainActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
                } else if (msg.what == 3) {         // 音调切换
                    int tunesAdd = msg.arg1;
                    RadioButton tuneRadio = (RadioButton) findViewById(getResources().getIdentifier("t" + tunesAdd + "_btn", "id", "com.example.mengchen.piano"));
                    tuneRadio.performClick();
                    Toast.makeText(MainActivity.this, "音调切换为 " + tuneRadio.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) return;
                if (timeTunesMap.isEmpty()) return;
                if (isPlaying) {
                    isPlaying = false;
                    playBtn.setText("播放");

                    if (playTimer != null) {
                        playTimer.cancel();
                        playTimer = null;
                    }
                } else {
                    isPlaying = true;
                    playBtn.setText("停止播放");

                    final AtomicLong playTick = new AtomicLong(0);
                    playTimer = new Timer();
                    playTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            long currentTick = playTick.incrementAndGet();
                            if (timeTunesMap.containsKey(currentTick)) {
                                List<RecordInfo> recordInfoList = timeTunesMap.get(currentTick);

                                // 结束标记
                                if (recordInfoList.isEmpty()) {
                                    playTimer.cancel();
                                    Message playEndMessage = new Message();
                                    playEndMessage.what = 2;
                                    handler.sendMessage(playEndMessage);
                                }

                                for (RecordInfo recordInfo : recordInfoList) {
                                    int tunes = recordInfo.getTunes();
                                    if (tunes < TUNE_VAL_STARTED) {
                                        playSound(recordInfo.getTunes(), null, null);
                                        Message message = new Message();
                                        message.what = 1;
                                        message.arg1 = recordInfo.getTunes();
                                        message.obj = recordInfo.getButton();
                                        handler.sendMessage(message);
                                    } else {
                                        Message message = new Message();
                                        message.what = 3;
                                        message.arg1 = tunes - TUNE_VAL_STARTED;
                                        handler.sendMessage(message);
                                    }
                                }
                            }
                        }
                    }, 0, RECORD_INTERVAL);
                }
            }
        });

        Button uploadBtn = (Button) this.findViewById(R.id.upload_btn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    Toast.makeText(MainActivity.this, "正在录音呢, 别急哦~", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (timeTunesMap.isEmpty()) {
                    Toast.makeText(MainActivity.this, "你好像没有录音呢，录音完成后再上传吧~", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", getString(R.string.url_upload_page));
                String recordJson = RecordInfoUtils.toJSON(timeTunesMap);
                intent.putExtra("recordJson", recordJson);
                intent.putExtra("recordTune", String.valueOf(tunesAdd));
                startActivityForResult(intent, 1);

            }
        });

        Button exploreBtn = (Button) this.findViewById(R.id.explore_btn);
        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, WebviewActivity.class);
                intent.putExtra("url", getString(R.string.url_list_page));
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SysApplication.getInstance().addActivity(this);

        // 保持屏幕常亮
        powerManager = (PowerManager) this.getSystemService(Activity.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        Bundle extras = data.getExtras();
        String recordJson = extras.getString("recordJson");
        final String recordTune = extras.getString("recordTune");
        if (recordJson != null) {
            timeTunesMap.clear();

            try {
                JSONArray ja = new JSONArray(recordJson);

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    long time = jo.getInt("time");
                    JSONArray tunesArr = jo.getJSONArray("tunes");

                    List<RecordInfo> recordInfoList = new ArrayList<>();

                    for (int j = 0; j < tunesArr.length(); j++) {
                        int tune = tunesArr.getInt(j);
                        recordInfoList.add(new RecordInfo(tune, null));
                    }

                    timeTunesMap.put(time, recordInfoList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



//            Toast.makeText(MainActivity.this, timeTunesMap.toString(), Toast.LENGTH_LONG).show();
            final Button playBtn = (Button) this.findViewById(R.id.play_btn);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RadioButton tuneRadio = (RadioButton) findViewById(getResources().getIdentifier("t" + recordTune + "_btn", "id", "com.example.mengchen.piano"));
                    tuneRadio.performClick();
                    Toast.makeText(MainActivity.this, "已自动切换到音调 " + tuneRadio.getText() + " 播放", Toast.LENGTH_SHORT).show();
                    playBtn.performClick();
                }
            }, 1000);
        }
    }

    private boolean playSound(int id, MotionEvent motionEvent, View view) {
        if (motionEvent == null || motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            soundPool.play(tunesSoundMap.get(id + tunesAdd), 1, 1, 0, 0, 1);

            if (isRecording) {
                long timestamp = (System.currentTimeMillis() - recordStartTime) / RECORD_INTERVAL;
                RecordInfo recordInfo = new RecordInfo(id, view);
                if (timeTunesMap.containsKey(timestamp)) {
                    timeTunesMap.get(timestamp).add(recordInfo);
                } else {
                    List<RecordInfo> recordInfoList = new ArrayList<>();
                    recordInfoList.add(recordInfo);
                    timeTunesMap.put(timestamp, recordInfoList);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long nowTimeStamp = System.currentTimeMillis();
            if (lastBackTimestamp == 0 || nowTimeStamp - lastBackTimestamp > 2000) {
                lastBackTimestamp = nowTimeStamp;
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                SysApplication.getInstance().exit();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }
}
