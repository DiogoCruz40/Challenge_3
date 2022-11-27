package pt.cm.challenge_3.Interfaces;

import java.util.List;

import pt.cm.challenge_3.database.entities.Point;
import pt.cm.challenge_3.dtos.PointDTO;

public interface PointMapperInterface {
     Point toEntityPoint(PointDTO pointDTO);
     PointDTO toPointDTO(Point point);
     List<Point> toEntityPoints(List<PointDTO> pointDTO);
     List<PointDTO> toPointsDTO(List<Point> points);
}
