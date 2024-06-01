package com.example.like_comment_service.DAOs;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.like_comment_service.Models.Like;


public interface likerepo extends JpaRepository<Like,Long> {

    public List<Like> findAllByObjid(Long postid);
    public Like findByObjid(Long postid);


    public void deleteAllByObjidIn(List<Long> postid);

    public void deleteAllByObjid(Long postid);

    public void deleteByAuthorName(String AuthorName);

    public void deleteByObjidAndAuthorName(Long objid,String author);

    public Like findByObjidAndAuthorName(Long objid,String author);
}
