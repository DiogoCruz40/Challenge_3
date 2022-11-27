package pt.cm.challenge_3.mappers;

import static pt.cm.challenge_3.mappers.MapperUtil.getMapper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

import pt.cm.challenge_3.Interfaces.PointMapperInterface;
import pt.cm.challenge_3.database.entities.Point;
import pt.cm.challenge_3.dtos.PointDTO;


public class PointMapper implements PointMapperInterface {

    ModelMapper modelMapper;

    public PointMapper()
    {
        modelMapper = getMapper();
    }

    @RequiresApi(api = Build.VERSION_CODES.N) //TODO: verficar se isto esta ok
    private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
    
    @Override
    public Point toEntityPoint(PointDTO pointDTO){
        return modelMapper.map(pointDTO, Point.class);
    }

    @Override
    public PointDTO toPointDTO(Point point)
    {
        return modelMapper.map(point, PointDTO.class);
    }

    @Override
    public List<Point> toEntityPoints(List<PointDTO> pointsDTO){
        return mapList(pointsDTO,Point.class);
    }

    @Override
    public List<PointDTO> toPointsDTO(List<Point> notes)
    {
        return mapList(notes,PointDTO.class);
    }

}
