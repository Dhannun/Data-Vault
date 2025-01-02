package ai.afrilab.datavault.mpastruct;

import ai.afrilab.datavault.datavault.DataVault;
import ai.afrilab.datavault.datavault.dto.DataVaultDto;
import ai.afrilab.datavault.datavault.textchunks.TextChunk;
import ai.afrilab.datavault.datavault.textchunks.dto.NoContentTextChunkDto;
import ai.afrilab.datavault.datavault.textchunks.dto.TextChunkContentDto;
import ai.afrilab.datavault.users.User;
import ai.afrilab.datavault.users.dto.UserDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapstructMapper {
  MapstructMapper INSTANCE = Mappers.getMapper(MapstructMapper.class);

  UserDto mapUserEntityToDto(User user);

  DataVaultDto mapDataVaultEntityToDto(DataVault dataVault);

  NoContentTextChunkDto mapTextChunkEntityToDto(TextChunk textChunk);

  TextChunkContentDto mapTextChunkToTextChunkContentDto(TextChunk textChunk);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  TextChunk partialUpdate(TextChunkContentDto textChunkContentDto, @MappingTarget TextChunk textChunk);

}
