package com.cts.rivio.mapper;

import com.cts.rivio.dto.response.CandidateDTO;
import com.cts.rivio.entity.Candidate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    // Extract the ID and Title from the linked JobOpening entity
    @Mapping(source = "jobOpening.id", target = "jobOpeningId")
    @Mapping(source = "jobOpening.title", target = "jobOpeningTitle")
    CandidateDTO toDto(Candidate entity);
}