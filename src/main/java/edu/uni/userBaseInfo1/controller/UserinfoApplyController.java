//package edu.uni.userBaseInfo1.controller;
//
//import edu.uni.administrativestructure.service.DepartmentService;
//import edu.uni.administrativestructure.service.PositionService;
//import edu.uni.administrativestructure.service.SubdepartmentService;
//import edu.uni.auth.mapper.RoleMapper;
//import edu.uni.auth.service.AuthService;
//import edu.uni.auth.service.RoleService;
//import edu.uni.bean.Result;
//import edu.uni.bean.ResultType;
//import edu.uni.place.service.FieldService;
//import edu.uni.professionalcourses.service.SpecialtyService;
//import edu.uni.userBaseInfo1.bean.*;
//import edu.uni.userBaseInfo1.mapper.UserinfoApplyApprovalMapper;
//import edu.uni.userBaseInfo1.service.*;
//import edu.uni.userBaseInfo1.utils.userinfoTransMapBean;
//import edu.uni.utils.RedisCache;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Author chenenru
// * @ClassName UserinfoApplyController
// * @Description
// * @Date 2019/4/30 15:52
// * @Version 1.0
// **/
////填写description内容可以在测试模块显示相应的文字和模块
//@Api(description = "用户信息申请信息模块")
////Controller类（或者说Http）的请求路径
////如果添加了路径，则在需要调用该类的方法时需要在方法请求mapping路径前加上类的mapping路径
//@RequestMapping("json/userinfoApply")
////标志这个类是一个controller类，用于被Spring扫描然后配置添加和配置相应的bean
//@Controller
//public class UserinfoApplyController {
//
//    //把UserinfoApply的Service层接口所有的方法自动装配到该对象中
//    //持久层接口的对象
//    @Autowired
//    private UserinfoApplyApprovalMapper userinfoApplyApprovalMapper;
//    @Autowired
//    private RoleMapper roleMapper;
//    @Autowired
//    UserinfoApplyService userinfoApplyService;
//    @Autowired
//    UserService userService;
//    @Autowired
//    UserinfoApplyApprovalController userinfoApplyApprovalController;
//    @Autowired
//    AuthService authService;
//    @Autowired
//    UserinfoApplyApprovalService userinfoApplyApprovalService;
//    @Autowired
//    private ApprovalStepInchargeService approvalStepInchargeService;
//    @Autowired
//    private OtherClassService otherClassService;
//    @Autowired
//    EmployeeService employeeService;
//    @Autowired
//    OtherEmployService otherEmployService;
//    @Autowired  //把Student的Service层接口所有的方法自动装配到该对象中
//    private StudentService studentService;
//    @Autowired
//    private StudentRelationService studentRelationService;
//    @Autowired
//    private EcommService ecommService;
//    @Autowired
//    private AddressService addressService;
//    @Autowired
//    private PictureService pictureService;
//    @Autowired
//    private LearningDegreeSerevice learningDegreeSerevice;
//    @Autowired
//    private EmployeeHistoryService employeeHistoryService;
//    @Autowired
//    private UserUploadFileService userUploadFileService;
//    @Autowired
//    private AddrCountryService addrCountryService;
//    @Autowired
//    private AddrStateService addrStateService;
//    @Autowired
//    private AddrCityService addrCityService;
//    @Autowired
//    private AddrAreaService addrAreaService;
//    @Autowired
//    private AddrStreetService addrStreetService;
//    @Autowired
//    private OtherUniversityService otherUniversityService;
//    @Autowired
//    private MyAcademicService myAcademicService;
//    @Autowired
//    private MyAcademicDegreeService myAcademicDegreeService;
//    @Autowired
//    private ApprovalMainService  approvalMainService;
//    @Autowired
//    private SpecialtyService specialtyService;
//    @Autowired
//    private FieldService fieldService;
//    @Autowired
//    private PoliticalAffiliationService politicalAffiliationService;
//    @Autowired
//    private DepartmentService departmentService;
//    @Autowired
//    private SubdepartmentService subdepartmentService;
//    @Autowired
//    private MySecondLevelDisciplineService mySecondLevelDisciplineService;
//    @Autowired
//    private OtherEmployPositionService otherEmployPositionService;
//    @Autowired
//    private PositionService positionService;
//    @Autowired
//    private RoleService roleService;
//    @Autowired
//    private OtherRoleService otherRoleService;
//
//
//    @Autowired  //把缓存工具类RedisCache相应的方法自动装配到该对象
//    private RedisCache cache;
//
//    //内部类，专门用来管理每个get方法所对应缓存的名称。
//    static class CacheNameHelper{
//        // ub1_e_UserinfoApply_{用户信息申请记录id}
//        public static final String Receive_CacheNamePrefix = "ub1_u_userinfoApply_";
//        // ub1_e_UserinfoApplys_listAll
//        public static final String ListAll_CacheName = "ub1_u_userinfoApply_listAll";
//    }
//
//
//
//    /**
//     * Author: mokuanyuan 15:19 2019/6/12
//     * @param map
//     * @return Result
//     * @apiNote: 在任何申请页面点击确认申请时
//     */
//    @ApiOperation(value="在任何申请页面点击确认申请时", notes="2019年5月11日 14:33:14 已通过测试")
//    @ApiImplicitParam( name = "map"  )
//    @PostMapping("/applyModify")
//    @ResponseBody
//    public Result ApplyModifyStudent(@RequestBody Map<String,Object> map) throws InvocationTargetException, IllegalAccessException {
//        User user = null;
//        edu.uni.auth.bean.User loginUser = authService.getUser();
//        if(loginUser == null){
//            return Result.build(ResultType.Failed, "你沒有登錄");
//        }
//        System.out.println(map.toString());
//        //获取前台参数
//        Integer type = (Integer) map.get("type");
//        String reason = (String) map.get("reason");
//        Integer modifiedUserId = (Integer) map.get("modifiedUserId");
//        if(type == null || reason == null)
//            return Result.build(ResultType.ParamError);
//        if( !(type >= 0 && type <= 10) )
//            return Result.build(ResultType.ParamError);
//
//        if(modifiedUserId != null) {
//            user = userService.selectUserById(modifiedUserId);
//            //只能同一个学校单位的用户才能做出操作
//            if(!user.getUniversityId().equals(loginUser.getUniversityId()))
//                return Result.build(ResultType.Disallow);
//        }
//
//        if(type == 6 || type == 9 ){ // 6和代表的是学生信息，无论是修改还是增加，而且9代表的是批量增加学生
//            //操作学生信息的发起者只能是辅导员,而且学生（被修改者）和辅导员（做出修改者）要在同一个学院
//            //如果不符合以上的条件，那么操作则被服务器拒绝或提示没有权限
//            if( modifiedUserId == null ) //必须提供被修改的学生用户的user_id
//                return Result.build(ResultType.ParamError);
//            //判断被修改这和修改者是否在同一个学院
//            if( ! userinfoApplyApprovalController.isDepartmentSame(user.getId(),loginUser.getId()))
//                return Result.build(ResultType.Disallow);
//            //再判断修改者是否有扮演辅导员这个角色
//            if( ! otherRoleService.isPlayOneRole(loginUser.getId(),"辅导员") )
//                return Result.build(ResultType.Disallow);
//        }
//
//        if( type == 4 || type == 5 || type == 7 || type == 10 ){
//            // 4 和 5 分别代表职员的学历和简历
//            // 7和代表的是职员信息，无论是修改还是增加，而且10代表的是批量增加职员
//            // 操作职员信息的发起者只能是人事处工作人员,而且职员（被修改者）和辅导员（做出修改者）要在同一个学校
//            // 如果不符合以上的条件，那么操作则被服务器拒绝或提示没有权限
//            if( ! otherRoleService.isPlayOneRole(loginUser.getId(),"人事处工作人员"))
//                return Result.build(ResultType.Disallow);
//            if( modifiedUserId == null ) //必须提供被修改的学生用户的user_id
//                return Result.build(ResultType.ParamError);
//            //判断修改者和被修改者是否是同一个学校单位
//            if(  ! loginUser.getUniversityId().equals(user.getUniversityId()) )
//                return Result.build(ResultType.Disallow);
//
//        }
//
//
//        return userinfoApplyService.clickApply((HashMap<String, Object>) map,type,loginUser,user) ?
//                Result.build(ResultType.Success) : Result.build(ResultType.Failed);
//
//    }
//
//
//    /**
//     * Author: mokuanyuan 14:56 2019/6/12
//     * @param map
//     * @return Result
//     * @apiNote: 审批所有申请, 点击通过或者不通过时
//     */
//    @ApiOperation(value="审批所有申请, 点击通过或者不通过时", notes="已测试 2019/6/5 21点43分")
//    @ApiImplicitParam( name = "map"  )
//    @PutMapping("judgeApply")
//    @ResponseBody
//    public Result commitApplyModifyStudent(@RequestBody Map<String,Object> map){
//        edu.uni.auth.bean.User loginUser = authService.getUser();
//        if(loginUser == null){
//            return Result.build(ResultType.Failed, "你沒有登錄");
//        }
//        Long userId = loginUser.getId();
//        Integer approvalId = (Integer) map.get("approval_id");
//        Integer flag = (Integer) map.get("flag");
//        String judgeReason = (String) map.get("Reason");
//        boolean isFlag = flag == -1 || flag == 0;
//
//        System.out.println(userId + "###" + approvalId + "$$" + flag + "@@" + judgeReason);
////        List<UserinfoApplyApproval> userinfoApplyApprovals = userinfoApplyApprovalService.selectByApplyId((long)approvalId);
//        UserinfoApplyApproval userinfoApplyApproval = userinfoApplyApprovalService.selectUserinfoApplyApprovalById((long) approvalId);
//        if(userId != null && userinfoApplyApproval != null && isFlag && judgeReason != null ){
//
//            userinfoApplyApproval.setReason(judgeReason);
//            if(flag.equals(0)){  // flag为0 时表示通过
//                //比较当前步骤是否是最后一步
//                if(userService.isLastStep(userinfoApplyApproval.getStep(),userinfoApplyApproval.getUserinfoApplyId())) //该步骤是最后一步
//                    return userService.endForPass(userinfoApplyApproval, userId) ?
//                            Result.build(ResultType.Success) : Result.build(ResultType.Failed);
//                else //该审批不是最后一步
//                    return userService.createForPass(userinfoApplyApproval, userId) ? Result.build(ResultType.Success) : Result.build(ResultType.Failed);
//            }
//            else{
//                if(flag.equals(-1)) // flag为-1 时表示不通过
//                    return userService.endForRefuse(userinfoApplyApproval, userId) ?
//                            Result.build(ResultType.Success) : Result.build(ResultType.Failed);
//                else
//                    return Result.build(ResultType.ParamError);
//            }
//
//        }
//        return Result.build(ResultType.ParamError);
//    }
//
//
//    /**
//     * Author: mokuanyuan 15:23 2019/6/12
//     * @param map
//     * @return List<UserinfoApply>
//     * @apiNote: 根据信息类型，申请结果和用户id查询该用户的所有申请信息
//     */
//    @ApiOperation( value = "根据信息类型，申请结果和用户id查询该用户的所有申请信息",notes = "未测试" )
//    @PostMapping("userinfoApplys/listAll")
//    @ApiImplicitParam(name = "map")
//    @ResponseBody
//    public Result selectAllByUserId(@RequestBody Map<String,Object> map) throws IOException {
//        //获取前端参数
//        UserinfoApply userinfoApply = new UserinfoApply();
//        userinfoApply.setInfoType( (Integer) map.get("type"));
//        userinfoApply.setApplyResult( (Boolean) map.get("result") );
//        //获取登录状态
//        edu.uni.auth.bean.User user = authService.getUser();
//        if(user == null){
//            return Result.build(ResultType.Failed, "你沒有登錄");
//        }
//        //检验参数合法性
//        if( user.getId() == null || userinfoApply.getInfoType() == null || userinfoApply.getApplyResult() == null )
//            return Result.build(ResultType.ParamError);
//
//        return Result.build(ResultType.Success).appendData("userinfoApplys",userinfoApplyService.
//                            selectByTypeAndResultAndUserId(userinfoApply,user.getId()));
//
//    }
//
//
//
//
//    /**
//     * Author: chenenru 23:44 2019/4/29
//     * @param response
//     * @return
//     * @apiNote: 获取所有用户信息申请记录的内容
//     */
//    @ApiOperation( value = "获取所有用户信息申请记录的内容",notes = "2019-5-2 11:09:47已通过测试" )
//    @GetMapping("userinfoApplys/listAll")
//    @ResponseBody
//    public void selectAll(HttpServletResponse response) throws IOException {
//        response.setContentType("application/json;charset=utf-8");
//        String cacheName = CacheNameHelper.ListAll_CacheName;
//        String json = cache.get(cacheName);
//        if(json == null){
//            json = Result.build(ResultType.Success)
//                    .appendData("userinfoApplys",userinfoApplyService.selectAllUserinfoApplys()).convertIntoJSON();
//            cache.set(cacheName,json);
//        }
//        response.getWriter().write(json);
//    }
//    /**
//     * Author: chenenru 23:47 2019/4/29
//     * @param  userinfoApply
//     * @return Result
//     * @apiNote: 新增用户信息申请信息
//     */
//    @ApiOperation(value="新增用户信息申请信息", notes="2019-5-2 11:09:51已通过测试")
//    @ApiImplicitParam(name = "userinfoApply", value = "用户信息申请详情实体", required = true, dataType = "UserinfoApply")
//    @PostMapping("/userinfoApply")  //post请求方式
//    @ResponseBody
//    public Result create(@RequestBody(required = false) UserinfoApply userinfoApply){
//        //检验页面传来的对象是否存在
//        if(userinfoApply != null){
//            boolean success = userinfoApplyService.insertUserinfoApply(userinfoApply);
//            if(success){
//                // 清空相关缓存
//                cache.delete(CacheNameHelper.ListAll_CacheName);
//                return Result.build(ResultType.Success);
//            }else{
//                return Result.build(ResultType.Failed);
//            }
//        }
//        return Result.build(ResultType.ParamError);
//    }
//    /**
//     * Author: chenenru 23:50 2019/4/29
//     * @param id
//     * @return Result
//     * @apiNote: 删除用户信息申请
//     */
//    @ApiOperation(value="删除用户信息申请", notes="2019-5-2 11:09:56已通过测试")
//    @ApiImplicitParam(name = "id", value = "用户信息申请的id", required = true, dataType = "Long", paramType = "path")
//    @DeleteMapping("/userinfoApply/{id}")   //delete请求
//    @ResponseBody
//    public Result destroy(@PathVariable Long id){
//        boolean success = userinfoApplyService.deleteUserinfoApply(id);
//        if(success){
//            // 清空相关缓存
//            cache.delete(CacheNameHelper.ListAll_CacheName);
//            return Result.build(ResultType.Success);
//        }else{
//            return Result.build(ResultType.Failed);
//        }
//    }
//    /**
//     * Author: chenenru 23:52 2019/4/29
//     * @param userinfoApply
//     * @return Result
//     * @apiNote: 更新用户信息申请详情
//     */
//    @ApiOperation(value="更新用户信息申请详情", notes="2019-5-2 11:10:01已通过测试")
//    @ApiImplicitParam(name = "userinfoApply", value = "用户信息申请详情实体", required = true, dataType = "UserinfoApply")
//    @PutMapping("/userinfoApply")   //Put请求
//    @ResponseBody
//    public Result update(@RequestBody(required = false) UserinfoApply userinfoApply){
//        if(userinfoApply != null && userinfoApply.getId() != null){
//            boolean success = userinfoApplyService.updateUserinfoApply(userinfoApply);
//            if(success){
//                //清除相应的缓存
//                cache.delete(CacheNameHelper.Receive_CacheNamePrefix + userinfoApply.getId());
//                cache.delete(CacheNameHelper.ListAll_CacheName);
//                return Result.build(ResultType.Success);
//            }else{
//                return Result.build(ResultType.Failed);
//            }
//        }
//        return Result.build(ResultType.ParamError);
//    }
//
//    //    /**
////     * Author: chenenru 23:41 2019/4/29
////     * @param id response
////     * @return response
////     * @apiNote: 获取用户信息申请详情
////     */
////    //以下说明为本类中所有方法的注解的解释，仅在本处注释（因为都几乎是一个模版）
////    //@ApiOperation：用于在swagger2页面显示方法的提示信息
////    //@GetMapping：规定方法的请求路径和方法的请求方式（Get方法）
////    //@ApiImplicitParam：用于在swagger2页面测试时用于测试的变量，详细解释可以看Swagger2注解说明
////    //@ResponseBody：指明该方法效果等同于通过response对象输出指定格式的数据（JSON）
////    @ApiOperation( value = "以一个id获取一条用户信息申请记录详情",notes = "2019-5-2 11:09:42已通过测试" )
////    @GetMapping("userinfoApply/{id}")
////    @ApiImplicitParam(name = "id", value = "userinfoApply表的一个id", required = false, dataType = "Long" , paramType = "path")
////    @ResponseBody
////    public void receive(@PathVariable Long id, HttpServletResponse response) throws IOException {
////        //设置返回的数据格式
////        response.setContentType("application/json;charset=utf-8");
////
////        userinfoApplyApprovalController.getOldInfoAndNewInfoByApply(id,response);
////    }
//
////    /**
////     * Author: laizhouhao 20:50 2019/5/9
////     * @param userinfoApplyApproval, user_id
////     * @return Result
////     * @apiNote: 审批申请信息, 点击通过时
////     */
////    @ApiOperation(value="审批申请信息, 点击通过时", notes="2019年5月14日 15:11:29 已通过测试")
////    @ApiImplicitParams({
////            @ApiImplicitParam(name = "userinfoApplyApproval", value = "用户申请审批流程表实体", required = true, dataType = "UserinfoApplyApproval"),
////            @ApiImplicitParam(name = "user_id", value = "审批人id", required = true, dataType = "Long", paramType = "path")
////    })
////    @PostMapping("commituserinfoApply/{user_id}")
////    @ResponseBody
////    public Result commitUserinfoApply(@RequestBody UserinfoApplyApproval userinfoApplyApproval, @PathVariable Long user_id){
////        if(userinfoApplyApproval != null){
////            //比较当前步骤是否是最后一步
////            boolean isLast = userService.isLastStep(userinfoApplyApproval.getStep(),userinfoApplyApproval.getUserinfoApplyId());
////            //该步骤是最后一步
////            if(isLast){
////                //更新申请表和用户信息审批流程表
////                boolean firstSuccess = userService.endForPass(userinfoApplyApproval, user_id);
////                //更新用户申请修改的信息的新旧记录
////                boolean thirdSuccess = userService.updateNewAndOldMessage(userinfoApplyApproval);
////                //判断两个更新是否都成功
////                if(firstSuccess) {
////                    //清除相应的缓存
////                    cache.delete(UserinfoApplyController.CacheNameHelper.Receive_CacheNamePrefix + "commitUserinfoApply");
////                    cache.delete(UserinfoApplyController.CacheNameHelper.ListAll_CacheName);
////                    return Result.build(ResultType.Success);
////                }else{
////                    return Result.build(ResultType.Failed);
////                }
////            }else{ //该审批不是最后一步
////                boolean secondSuccess = userService.createForPass(userinfoApplyApproval, user_id);
////                //操作成功
////                if(secondSuccess){
////                    //清除相应的缓存
////                    cache.delete(UserinfoApplyController.CacheNameHelper.Receive_CacheNamePrefix + "commitUserinfoApply");
////                    cache.delete(UserinfoApplyController.CacheNameHelper.ListAll_CacheName);
////                    return Result.build(ResultType.Success);
////                }else{
////                    return Result.build(ResultType.Failed);
////                }
////            }
////        }
////        return Result.build(ResultType.ParamError);
////    }
////
////    /**
////     * Author: laizhouhao 20:50 2019/5/9
////     * @param userinfoApplyApproval,user_id
////     * @return Result
////     * @apiNote: 审批申请信息, 点击不通过时
////     */
////    @ApiOperation(value="审批申请信息, 点击不通过时", notes="2019年5月14日 15:11:21 已通过测试")
////    @ApiImplicitParams({
////            @ApiImplicitParam(name = "userinfoApplyApproval", value = "用户申请审批流程表实体", required = true, dataType = "UserinfoApplyApproval"),
////            @ApiImplicitParam(name = "user_id", value = "审批人id", required = true, dataType = "Long", paramType = "path")
////    })
////    @PostMapping(value = "refuseuserinfoApply/{user_id}",consumes = MediaType.APPLICATION_JSON_VALUE)
////    @ResponseBody
////    public Result refuseUserinfoApply(@RequestBody UserinfoApplyApproval userinfoApplyApproval,@PathVariable Long user_id) throws IOException {
////        System.out.println("小莫是头猪！！！---");
////        if(userinfoApplyApproval != null && user_id != null){
////            boolean success = userService.endForRefuse(userinfoApplyApproval, user_id);
////            if(success) {
////                //清除相应的缓存
////                cache.delete(UserinfoApplyController.CacheNameHelper.Receive_CacheNamePrefix + "refuseUserinfoApply");
////                cache.delete(UserinfoApplyController.CacheNameHelper.ListAll_CacheName);
////                return Result.build(ResultType.Success);
////            }else{
////                return Result.build(ResultType.Failed);
////            }
////        }
////        return Result.build(ResultType.ParamError);
////    }
//
//}
