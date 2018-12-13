package travel.office;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "trips", path = "trips")
public interface TripRepository extends PagingAndSortingRepository<Trip, Long>
{
    List<Trip> findByDest(@Param("dest") String dest);
}
