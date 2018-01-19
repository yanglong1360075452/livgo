package com.wizinno.livgo.manage.controller;
import com.wizinno.livgo.app.data.live.LiveExtend;
import com.wizinno.livgo.app.data.live.LiveHistory;
import com.wizinno.livgo.app.data.live.LiveSimple;
import com.wizinno.livgo.app.data.user.Audience;
import com.wizinno.livgo.app.document.InOut;
import com.wizinno.livgo.app.document.Live;
import com.wizinno.livgo.app.repository.InoutRepository;
import com.wizinno.livgo.app.repository.LiveRepository;
import com.wizinno.livgo.app.repository.LogRepository;
import com.wizinno.livgo.app.repository.UserRepository;
import com.wizinno.livgo.app.utils.LiveTransUtil;
import com.wizinno.livgo.app.utils.Util;
import com.wizinno.livgo.data.PageData;
import com.wizinno.livgo.data.ResponseVO;
import com.wizinno.livgo.exception.CustomException;
import com.wizinno.livgo.exception.CustomExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LiuMei on 2017-05-22.
 */
@RestController
@RequestMapping("/api/manage/lives")
public class LivesController {

    @Autowired
    private LiveRepository liveRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InoutRepository inoutRepository;
    /**
     * 获取当前/历史直播列表
     * @param page
     * @param length
     * @param status
     * @return
     */

    @RequestMapping(method = RequestMethod.GET)
    public ResponseVO getLives(@RequestParam(value = "page",defaultValue = "0") int page,
                               @RequestParam(value = "length",defaultValue = "10") int length,
                               @RequestParam(value = "liveUsername",required = false) String liveUsername,
                               @RequestParam(value = "deviceNumber",required = false) String deviceNumber,
                               @RequestParam(value = "createTime",required = false) Long createTime,
                               @RequestParam(value = "endTime",required = false) Long endTime,
                               @RequestParam(value = "status",required = false) Integer status) {
        Query query = new Query();
        if(!Util.stringNull(liveUsername)){
            query.addCriteria(Criteria.where("user.username").regex(liveUsername));
        }
        if(status==0){
            query.addCriteria(Criteria.where("endTime").is(null));
        }else{
            query.addCriteria(Criteria.where("endTime").ne(null));
            if(createTime !=null&& endTime!=null){
                if(createTime>0&& endTime>0){
                    query.addCriteria(Criteria.where("startTime").gt(new Date(createTime)).lt(new Date(endTime)));
                }
            }
        }
        if(!Util.stringNull(deviceNumber)){
            query.addCriteria(Criteria.where("user.username").regex(deviceNumber));
        }
        Sort sort=new Sort(new Sort.Order(Sort.Direction.DESC,"startTime"));
        query.with(sort);
        Pageable pageable = new PageRequest(page,length);
        Page<Live> livePage = liveRepository.find(query,pageable);
        List<LiveSimple> liveSimple=LiveTransUtil.livesToSimples(livePage.getContent());
        return new ResponseVO(new PageData<>(page,length,liveSimple,livePage.getTotalPages(),livePage.getTotalElements()));
    }
    /**
     * 根据直播id获取直播详情
     * @param liveId
     * @return
     */
    @RequestMapping(value = "/{liveId}", method = RequestMethod.GET)
    public ResponseVO liveDetail(@PathVariable("liveId")Long liveId){
      return new ResponseVO(LiveTransUtil.liveToExtend(liveRepository.findById(liveId)));
    }
    /**
     * 获取正在看直播的观众列表
     * @param liveId  直播id
     * @return
     */
    @RequestMapping(value = "/liveAudience", method = RequestMethod.GET)
    public ResponseVO liveAudience(@RequestParam("liveId") Long liveId,
                                   @RequestParam(value = "currentPage",defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize",defaultValue = "10") int length){
        if(liveId==0){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        Live live= liveRepository.findById(liveId);
        if(live.getEndTime()==null){
            List<Audience>audiences=new ArrayList<>();
            Query query=new Query();
            query.addCriteria(Criteria.where("liveId").is(liveId));
            query.addCriteria(Criteria.where("userId").ne(null));
            query.addCriteria(Criteria.where("inTime").ne(null));
            query.addCriteria(Criteria.where("outTime").is(null));
            Pageable pageable = new PageRequest(page-1,length);
            Page<InOut> inOuts = inoutRepository.find(query,pageable);
            List<LiveHistory> liveHistories=LiveTransUtil.liveHistoryList(inOuts.getContent());
            return new ResponseVO(new PageData(page,length,liveHistories,inOuts.getTotalPages(),inOuts.getTotalElements()));
        }else{
            return new ResponseVO();
        }


    }
    /**
     * 获取直播的历史观众
     * @param liveId  直播id
     * @return
     */
    @RequestMapping(value = "/historyAudience", method = RequestMethod.GET)
    public ResponseVO historyAudience(@RequestParam("liveId") Long liveId,
                                      @RequestParam(value = "currentPage",defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int length){
        if(liveId<=0){
            throw new CustomException(CustomExceptionCode.ErrorParam);
        }
        List<Audience>audiences=new ArrayList<>();
        Live live= liveRepository.findById(liveId);
        Query query=new Query();
        query.addCriteria(Criteria.where("liveId").is(liveId));
        query.addCriteria(Criteria.where("userId").ne(null));
        query.addCriteria(Criteria.where("inTime").ne(null));
        query.addCriteria(Criteria.where("outTime").ne(null));
        Pageable pageable = new PageRequest(page-1,length);
        Page<InOut> inOuts = inoutRepository.find(query,pageable);
        List<LiveHistory> liveHistories=LiveTransUtil.liveHistoryList(inOuts.getContent());
        return new ResponseVO(new PageData(page,length,liveHistories,inOuts.getTotalPages(),inOuts.getTotalElements()));

    }

    /**
     * 用户直播列表
     * @param userId  用户id
     * @return
     */
    @RequestMapping(value = "/liveList", method = RequestMethod.GET)
    public ResponseVO historyAudience(@RequestParam(value = "currentPage",defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int length,
                                      @RequestParam("userId") Long userId){
        Query query=new Query();
        Sort sort=new Sort(new Sort.Order(Sort.Direction.DESC,"startTime"));
        query.addCriteria(Criteria.where("user.id").is(userId));
        query.with(sort);
        Pageable pageable = new PageRequest(page-1,length);
        Page<Live> livePage = liveRepository.find(query,pageable);
        List<LiveExtend> liveExtends=LiveTransUtil.liveToExtends(livePage.getContent());
        return new ResponseVO(new PageData(page,length,liveExtends,livePage.getTotalPages(),livePage.getTotalElements()));
    }
    /**
     * 用户观看历史
     * @param userId  用户id
     * @return
     */
    @RequestMapping(value = "/seeLiveList", method = RequestMethod.GET)
    public ResponseVO seeLiveList(@RequestParam(value = "currentPage",defaultValue = "1") int page,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int length,
                                      @RequestParam("userId") Long userId) {
        Query query = new Query();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "inTime"));
        query.addCriteria(Criteria.where("userId").is(userId));
        Pageable pageable = new PageRequest(page - 1, length);
        query.with(sort);
        Page<InOut> lists = inoutRepository.find(query, pageable);
        List<LiveHistory> liveHistories = LiveTransUtil.liveHistoryList(lists.getContent());
        return new ResponseVO(new PageData(page, length, liveHistories, lists.getTotalPages(), lists.getTotalElements()));

    }
}
