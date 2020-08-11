package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Entity.EmailEntity;
import cn.edu.njnu.geoproblemsolving.domain.activity.Project;
import cn.edu.njnu.geoproblemsolving.domain.activity.service.ProjectService;
import cn.edu.njnu.geoproblemsolving.View.StaticPagesBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;
    private final StaticPagesBuilder staticPagesBuilder;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
        this.staticPagesBuilder = new StaticPagesBuilder();
    }

    /**
     * Conditional query
     * @param category
     * @param tag
     * @param keyword
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object inquiryByPage(@RequestParam("category") String category,
                                @RequestParam("tag") String tag,
                                @RequestParam("keyword") String keyword,
                                @RequestParam("page") int page,
                                @RequestParam("pageSize") int pageSize){
        Object result = projectService.inquiryByConditions(category, tag, keyword, page , pageSize);
        return result;
    }

    /**
     * Create project
     * @param project
     * @return
     * @throws IOException
     */
    @RequestMapping(produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object createProject(@RequestBody Project project) throws IOException {
        logger.info("createProject");
        Object result = projectService.createProject(project);

        // Thymeleaf
        if(result instanceof Project){
            staticPagesBuilder.projectDetailPageBuilder((Project) result);
            staticPagesBuilder.projectListPageBuilder(projectService.findProjectsByPage(0,18));
        }

        return result;
    }

    /**
     * Updata project
     * @param project
     * @return
     * @throws IOException
     */
    @RequestMapping(produces = {"application/json;charset=UTF-8"}, method = RequestMethod.PUT)
    public Object updateProject(@RequestBody Project project) throws IOException {
        Object result = projectService.updateProject(project);
        // Thymeleaf
        if(result.equals("Success")) {
            staticPagesBuilder.projectDetailPageBuilder((Project) result);
            staticPagesBuilder.projectListPageBuilder(projectService.findProjectsByPage(0, 18));
        }
        return result;
    }

    /**
     * Delete project
     * @param aid
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteProject(@RequestParam("aid") String aid) {
        String result = projectService.deleteProject(aid);
        // Thymeleaf
        if(result.equals("Success")) {
            staticPagesBuilder.projectDetailPageRemove(aid);
            staticPagesBuilder.projectListPageBuilder(projectService.findProjectsByPage(0, 18));
        }
        return result;
    }

    /**
     * Inquiry the creator and members of one project
     * @param aid
     * @return
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.GET)
    public Object getParticipants(@PathVariable("aid") String aid){
        Object result = projectService.findParticipants(aid);
        return result;
    }

    /**
     * Add a member to a project
     * @param aid
     * @param userId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.POST)
    public Object joinProject(@PathVariable("aid") String aid, @RequestParam("userId") String userId) throws IOException {
        Object result = projectService.joinProject(aid, userId);

        if(result instanceof Project) {
            staticPagesBuilder.projectDetailPageBuilder((Project) result);
//            staticPagesBuilder.projectListPageBuilder(projectService.findProjectsByPage(0, 18));
        }
        return result;
    }

    /**
     * Delete a member of a project
     * @param aid
     * @param userId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.DELETE)
    public Object quitProject(@PathVariable("aid") String aid, @RequestParam("userId") String userId) throws IOException {
        Object result = projectService.quitProject(aid, userId);

        if(result instanceof Project) {
            staticPagesBuilder.projectDetailPageBuilder((Project) result);
//            staticPagesBuilder.projectListPageBuilder(projectService.findProjectsByPage(0, 18));
        }
        return result;
    }

    /**
     * Update a member role
     * @param aid
     * @param userId
     * @param role
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{aid}/user", method = RequestMethod.PUT)
    public Object changeUserRole(@PathVariable("aid") String aid, @RequestParam("userId") String userId, @RequestParam("role") String role) throws IOException {
        Object result = projectService.updateMemberRole(aid, userId, role);

        if(result instanceof Project) {
            staticPagesBuilder.projectDetailPageBuilder((Project) result);
        }
        return result;
    }

    /**
     * join a project via email
     * @param aid
     * @param email
     * @param password
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/{aid}/invited", method = RequestMethod.POST)
    public Object joinByMail(@PathVariable("aid") String aid, @RequestParam("email") String email, @RequestParam("password") String password) throws IOException {
        Object result = projectService.invitedParticipants(aid, email, password);

        if(result instanceof Project) {
            staticPagesBuilder.projectDetailPageBuilder((Project) result);
//            staticPagesBuilder.projectListPageBuilder(projectService.findProjectsByPage(0, 18));
        }
        return result;
    }

    /**
     * send emails to apply to join a project
     * @param aid
     * @param emailEntity
     * @return
     */
    @RequestMapping(value = "/{aid}/apply", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String sendEmail(@PathVariable("aid") String aid, @RequestBody EmailEntity emailEntity) {
        return projectService.applyJoinProject(aid, emailEntity);
    }
}
