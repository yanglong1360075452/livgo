package com.wizinno.livgo.app;
import com.wizinno.livgo.app.document.Live;
import com.wizinno.livgo.app.repository.LiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class UpdateLive  {
    @Autowired
    private LiveRepository liveRepository;
    public void run(){
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    Query query=new Query();
                    query.addCriteria(Criteria.where("endTime").is(null));
                    List<Live> lives= liveRepository.findByQuerys(query);
                    Long pollTime=(long)0;
                    if(lives!=null&&lives.size()>0){
                        for(Live live:lives){
                            Date date=live.getPackageTime();
                            if(date!=null){
                               pollTime=live.getPackageTime().getTime();
                            }

                            Long time=(new Date().getTime()-pollTime)/(1000*60);
                            if(time>10){
                                //将直播间关闭
                                Query query1=new Query();
                                Update update1=new Update();
                                query1.addCriteria(Criteria.where("id").is(live.getId()));
                                update1.set("endTime",new Date());
                                liveRepository.update(query1,update1);
                                System.out.println("******************");
                                System.out.println("更新直播表");
                            }
                        }
                    }

                }
            }, 1000*2);// 设定指定的时间time,此处为2000毫秒
        }
    }


