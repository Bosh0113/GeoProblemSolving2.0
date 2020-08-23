package cn.edu.njnu.geoproblemsolving.business.activity.controller;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Subproject;
import cn.edu.njnu.geoproblemsolving.business.activity.service.SubprojectService;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/subproject")
public class SubProjectController {

    private final SubprojectService subprojectService;

    public SubProjectController(SubprojectService subprojectService) {
        this.subprojectService = subprojectService;
    }

    /**
     * create subproject
     * @param subproject
     * @return
     */
    @RequestMapping(produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public Object createSubproject(@RequestBody Subproject subproject){
        return subprojectService.createSubproject(subproject);
    }

    /**
     * inquiry subproject
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public Object readSubproject(@RequestParam("projectId") String projectId){
        return subprojectService.inquirySubprojects(projectId);
    }

    /**
     * get participants of one subproject
     * @param aid
     * @return
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.GET)
    public Object getSubprojectParticipants(@PathVariable("aid") String aid){
        return subprojectService.findParticipants(aid);
    }

    /**
     * join a subproject
     * @param aid
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.POST)
    public Object joinSubproject(@PathVariable("aid") String aid, @RequestParam("userId") String userId){
        return subprojectService.joinSubproject(aid, userId);
    }

    /**
     * change the role of member
     * @param aid
     * @param userId
     * @param role
     * @return
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.PUT)
    public Object changeUserRole(@PathVariable("aid") String aid, @RequestParam("userId") String userId, @RequestParam("role") String role){
        return subprojectService.updateMemberRole(aid, userId, role);
    }

    /**
     * Exit a subproject
     * @param aid
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.DELETE)
    public String quitSubproject(@PathVariable("aid") String aid, @RequestParam("userId") String userId){
        return subprojectService.quitSubproject(aid, userId);
    }

}
