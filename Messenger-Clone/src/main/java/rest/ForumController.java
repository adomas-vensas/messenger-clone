package rest;

import DTOs.ForumCreationRequest;
import DTOs.ForumDTO;
import entities.Forum;
import entities.Group;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import lombok.Setter;
import services.ForumService;
import services.GroupService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/forums")
public class ForumController {

    @Inject
    private ForumService forumService;

    @Inject
    private GroupService groupService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForums()
    {
        List<Forum> allForums = forumService.getAllForums();
        return Response.ok(
                allForums
                    .stream()
                    .map(this::getForumDTO)
                    .collect(Collectors.toList())
        ).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForum(@PathParam("id") final long forumId)
    {
        Forum forum = forumService.findForumById(forumId);

        if(forum == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(getForumDTO(forum)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response insertForum(ForumCreationRequest request)
    {
        Set<Group> groups = request.getGroupIds().stream()
                .map(groupId -> groupService.findById(groupId))
                .collect(Collectors.toSet());

        Forum newForum = new Forum();
        newForum.setName(request.getName());
        newForum.setGroups(groups);

        forumService.saveForum(newForum);

        for(Group group : groups)
        {
            group.setForum(newForum);
            groupService.update(group);
        }

        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateForum(@PathParam("id") final Long forumId, ForumCreationRequest request)
    {
        Forum forum = forumService.findForumById(forumId);

        if (forum == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Set<Group> groups = request.getGroupIds().stream()
                .map(groupId -> groupService.findById(groupId))
                .collect(Collectors.toSet());

        if (groups.size() < request.getGroupIds().size())
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        forum.setGroups(groups);

        try
        {
            forumService.updateForum(forum);
            for(Group group : groups)
            {
                group.setForum(forum);
                groupService.update(group);
            }
        }
        catch (OptimisticLockException ole)
        {
            return Response.status(Response.Status.CONFLICT).build();
        }

        return Response.ok(Response.Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public CompletionStage<Response> deleteForum(@PathParam("id") final Long forumId)
    {
        return CompletableFuture.supplyAsync(() -> {

            Forum forum = forumService.findForumById(forumId);

            if (forum == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            for (Group group : forum.getGroups()) {
                group.setForum(null);
                groupService.update(group);
            }

            forumService.deleteForum(forumId);

            return Response.noContent().build();
        });
    }



    private ForumDTO getForumDTO(Forum forum)
    {
        List<Long> groupIds = forum.getGroups().stream()
                .map(Group::getId)
                .collect(Collectors.toList());

        ForumDTO forumDTO = new ForumDTO();

        forumDTO.setGroupIds(groupIds);
        forumDTO.setName(forum.getName());
        forumDTO.setId(forum.getId());

        return forumDTO;
    }


}
