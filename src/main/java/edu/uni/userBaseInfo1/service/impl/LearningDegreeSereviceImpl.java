package edu.uni.userBaseInfo1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.uni.administrativestructure.service.UniversityService;
//import edu.uni.auth.bean.Role;
//import edu.uni.auth.mapper.RoleMapper;
import edu.uni.example.config.ExampleConfig;
import edu.uni.userBaseInfo1.bean.*;
import edu.uni.userBaseInfo1.bean.LearningDegree;
import edu.uni.userBaseInfo1.mapper.*;
import edu.uni.userBaseInfo1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author chenenru
 * @ClassName LearningDegreeSereviceImpl
 * @Description
 * @Date 2019/4/30 15:11
 * @Version 1.0
 **/
//Service类的注解，标志这是一个服务层接口类，这样才能被Spring”“”“”“”"扫描"到=
@SuppressWarnings("ALL")
@Service
public class LearningDegreeSereviceImpl implements LearningDegreeSerevice {
    //持久层接口的对象
    @Autowired
    private LearningDegreeMapper learningDegreeMapper;
//    @Autowired
//    private RoleMapper roleMapper;
    @Autowired
    private ApprovalStepInchargeService approvalStepInchargeService;
    @Autowired
    private UserinfoApplyApprovalService userinfoApplyApprovalService;
    @Autowired
    private ApprovalMainService approvalMainService;
    @Autowired
    private UserinfoApplyService userinfoApplyService;
    @Autowired
    private AddrCountryService addrCountryService;
    @Autowired
    private AddrCityService addrCityService;
    @Autowired
    private OtherUniversityService otherUniversityService;
    @Autowired
    private MyAcademicDegreeService myAcademicDegreeService;
    @Autowired
    private MyAcademicService myAcademicService;


    /*@Autowired
    private AcademicMapper academicMapper;
    @Autowired
    private AcademicDegreeMapper academicDegreeMapper;*/
    //配置类，规定了上传文件的路径和分页查询每一页的记录数
    @Autowired
    private ExampleConfig config;

    /**
     * Author: chenenru 0:10 2019/4/30
     * @param
     * @return  List<LearningDegree>
     * @apiNote: 查询所有学历记录，不分页
     */
    @Override
    public List<LearningDegree> selectAllLearningDegrees() {
        return learningDegreeMapper.selectByExample(null);
    }
    /**
     * Author: chenenru 0:10 2019/4/30
     * @param id
     * @return LearningDegree
     * @apiNote: 通过id查询一个学历记录
     */
    @Override
    public LearningDegree selectLearningDegreeById(long id) {
//        LearningDegreeExample example = new LearningDegreeExample();
//        LearningDegreeExample.Criteria criteria = example.createCriteria();
//        criteria.andDegreeIdEqualTo(id);
        return learningDegreeMapper.selectByPrimaryKey(id);
//        return  learningDegreeMapper.selectByExample(example).get(0);
    }

    /**
     * Author: laizhouhao 8:30 2019/5/6
     * @param user_id
     * @return List<LearningDegree>
     * @apiNote:
     */
    @Override
    public List<LearningDegree> selectByUserId(Long user_id) {
        LearningDegreeExample example = new LearningDegreeExample();
        LearningDegreeExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(user_id);
        criteria.andDeletedEqualTo(false);
        //return learningDegreeMapper.selectByUserId(user_id);
        return  learningDegreeMapper.selectByExample(example);
    }

    /**
     * Author: chenenru 0:10 2019/4/30
     * @param pageNum
     * @return  PageInfo<LearningDegree>
     * @apiNote: 分页查询所有学历记录
     */
    @Override
    public PageInfo<LearningDegree> selectLearningDegreeByPage(int pageNum) {
        //设置查询的是哪一页和每一页有多少个记录
        PageHelper.offsetPage(pageNum,config.getPageSize());
        //无条件查询（条件对象为null，即无条件），查询所有
        List<LearningDegree> LearningDegrees = learningDegreeMapper.selectByExample(null);
        //检验查询的结果
        if(LearningDegrees !=null){
            return new PageInfo<>(LearningDegrees);
        }else{
            return null;
        }
    }
    /**
     * Author: chenenru 0:10 2019/4/30
     * @param LearningDegree
     * @return boolean
     * @apiNote: 插入一条学历记录
     */
    @Override
    public boolean insertLearningDegree(LearningDegree LearningDegree) {
        return  learningDegreeMapper.insertSelective(LearningDegree) > 0 ? true : false;
    }
    /**
     * Author: chenenru 0:10 2019/4/30
     * @param LearningDegree
     * @return boolean
     * @apiNote: 更新一条学历记录
     */
    @Override
    public boolean updateLearningDegree(LearningDegree LearningDegree) {
        return learningDegreeMapper.updateByPrimaryKeySelective(LearningDegree) >0 ? true : false;
    }
    /**
     * Author: chenenru 0:10 2019/4/30
     * @param id
     * @return boolean
     * @apiNote:  删除一条学历记录
     */
    @Override
    public boolean deleteLearningDegree(long id) {
        return learningDegreeMapper.deleteByPrimaryKey(id) > 0 ? true :false;
    }

    /**
     * Author: laizhouhao 18:13 2019/5/10
     * @param user_id
     * @return List<LearningDegree>
     * @apiNote: 根据用户id获取有效的学历信息
     */
    @Override
    public List<LearningDegree> selectValidLeaDeByUserId(Long user_id) {
        LearningDegreeExample learningDegreeExample = new LearningDegreeExample();
        learningDegreeExample.createCriteria().andUserIdEqualTo(user_id).andDeletedEqualTo(false);
        List<LearningDegree> learningDegreeList = new ArrayList<>();
        learningDegreeList = learningDegreeMapper.selectByExample(learningDegreeExample);
        return learningDegreeList;
    }

    /**
     * Author: chenenru 20:07 2019/5/13
     * @param requestMessage
     * @return boolean
     * @apiNote: 用户点击修改学历
     */
    /*@Override
    public boolean clickApplyLearningDegree(RequestMessage requestMessage) {
        LearningDegree learningDegree = requestMessage.getLearningDegree();
        Long byWho = requestMessage.getByWho();
        UserinfoApply userInfo_apply = requestMessage.getUserinfoApply();
        //获取被修改的用户id
        Long user_id = learningDegree.getUserId();
        //旧记录id
        Long oldId = learningDegree.getId();
//            System.out.println("oldId = "+oldId);
        //将要插入的记录设置为无效
        learningDegree.setDeleted(true);
        //将新纪录插入Ecomm表
        learningDegreeMapper.insert(learningDegree);
        //新纪录的id
        Long newId = learningDegree.getId();
        //向userinfoApply增加审批业务id
        userInfo_apply.setApprovalMainId(approvalMainService.
                selectIdByName(userInfo_apply.getUniversityId(), "审批学生申请修改学历"));
        //设置用户信息申请种类
        userInfo_apply.setInfoType(0);
        //设置用户信息申请旧信息记录id
        userInfo_apply.setOldInfoId(oldId);
        //设置用户信息申请新信息记录id
        userInfo_apply.setNewInfoId(newId);
        //设置用户信息申请开始时间
        userInfo_apply.setStartTime(learningDegree.getDatetime());
        //设置用户信息创建时间
        userInfo_apply.setDatetime(learningDegree.getDatetime());
        //设置用户信息申请写入者
        userInfo_apply.setByWho(byWho);
        //设置用户信息申请为有效
        userInfo_apply.setDeleted(false);
        //插入新的userinfoApply记录
        boolean successInfoApply = userinfoApplyService.insertUserinfoApply(userInfo_apply);
        //向审批流程表插入一条数据
        UserinfoApplyApproval applyApproval = new UserinfoApplyApproval();
        //设置学校id
        applyApproval.setUniversityId(userInfo_apply.getUniversityId());
        //设置申请表id
        applyApproval.setUserinfoApplyId(userInfo_apply.getId());
        //设置步骤，初始化为1
        applyApproval.setStep(1);
        //设置时间
        applyApproval.setDatetime(userInfo_apply.getStartTime());
        //设置写入者
        applyApproval.setByWho(byWho);
        //设置为有效
        applyApproval.setDeleted(false);
        //设置申请信息的种类
        applyApproval.setInfoType(userInfo_apply.getInfoType());
        //设置审批的角色名
        int st = applyApproval.getStep();
        Long mainId = userInfo_apply.getApprovalMainId();
        Long roleId = approvalStepInchargeService
                .selectRoleIdByStepAppovalId(st,mainId);
        Role role = roleMapper.selectByPrimaryKey(roleId);
        //设置申请人的用户id
        applyApproval.setApplyUserId(byWho);
        boolean successApplyApproval = userinfoApplyApprovalService.insertUserinfoApplyApproval(applyApproval);
        System.out.println("aaa="+applyApproval);
        return successInfoApply && successApplyApproval;
    }*/

    /**
     * Author: mokuanyuan 20:14 2019/6/2
     * @param map
     * @param address
     * @apiNote: 传入一个HashMap和Address对象，把Address里的id字段对应的信息内容放入到map里
     */
    @Override
    public void selectAllInfoToMap(HashMap map, LearningDegree learningDegree) {
        map.put("country",addrCountryService.selectAddrCountryById(learningDegree.getCountryId()).getCountryZh());
        map.put("city",addrCityService.selectAddrCityById(learningDegree.getCityId()).getCityZh());
        map.put("school", otherUniversityService.selectValidById(learningDegree.getSchoolId()).getName());
        map.put("academic", myAcademicService.selectById(learningDegree.getAcademicId()).getName());
        map.put("degree", myAcademicDegreeService.selectById(learningDegree.getDegreeId()).getName());

    }

    /**
     * Author: mokuanyuan 14:52 2019/6/12
     * @param oldId
     * @param newId
     * @return boolean 操作结果
     * @apiNote: 当审批的最后一步都通过后进行的操作，把相应的信息记录进行更新操作
     */
    public boolean updateForApply(Long oldId,Long newId){
        boolean result = false;
        LearningDegree newLearningDegree = selectLearningDegreeById(newId);
        if(oldId != null){
            LearningDegree oldLearningDegree = selectLearningDegreeById(oldId);
            oldLearningDegree.setId(newId);
            newLearningDegree.setId(oldId);
            if( updateLearningDegree(oldLearningDegree) && updateLearningDegree(newLearningDegree) )
                result = true;
        }else{
            newLearningDegree.setDeleted(false);
            if( updateLearningDegree(newLearningDegree) )
                result = true;
        }
        return result;
    }


    /**
     * Author: laizhouhao 16:28 2019/6/10
     * @param learningDgreeList
     * @return 用户的学历信息
     * @apiNote: 根据用户的所有学历信息实体获取用户的所有学历信息详情
     */
    @Override
    public void getLearningDegree(HashMap<String, Object> map, List<LearningDegree> learningDgreeList) {
        //获取用户的所有学历信息，并将放入map中
        for (int i=0; i<learningDgreeList.size(); i++){
            //判断该学历信息是否有效，有效则加入
            if(learningDgreeList.get(i).getDeleted() == false){
                map.put("id", learningDgreeList.get(i).getId());
                map.put("BeginTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningDgreeList.get(i).getBeginTime()));
                map.put("EndTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(learningDgreeList.get(i).getEndTime()));
                map.put("Country",addrCountryService.selectAddrCountryById(learningDgreeList.get(i).getCountryId()).getCountryZh());
                map.put("City", addrCityService.selectAddrCityById(learningDgreeList.get(i).getCityId()).getCityZh());
                map.put("School","肇庆学院");
                map.put("Acdemic", myAcademicService.selectById(learningDgreeList.get(i).getAcademicId()).getName());
                map.put("AcademicDegree", myAcademicDegreeService.selectById(learningDgreeList.get(i).getDegreeId()).getName());
            }
        }
    }
}
