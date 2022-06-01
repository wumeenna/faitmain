package com.faitmain.domain.example.mapper;

import com.faitmain.domain.example.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface MemberMapper{

    ArrayList< HashMap< String, Object > > findAll();

    Member getMember( int id );

    List< Member > getMemberList();

    int createMember( Member member );

    int updateMember( Member member );

    int deleteMember( int id );
}