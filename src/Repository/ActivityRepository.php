<?php

namespace App\Repository;

use App\Entity\Activity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Activity>
 */
class ActivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Activity::class);
    }
    public function findAllWithCategories()
{
    return $this->createQueryBuilder('a')
        ->leftJoin('a.categorieActivity', 'c')
        ->addSelect('c')
        ->getQuery()
        ->getResult();
}

// src/Repository/ActivityRepository.php

public function findWithSalles(int $id): ?Activity
{
    return $this->createQueryBuilder('a')
        ->select('a', 'p', 's')
        ->leftJoin('a.plannings', 'p')
        ->leftJoin('p.salle', 's')
        ->where('a.idActivity = :id')
        ->setParameter('id', $id)
        ->getQuery()
        ->getOneOrNullResult();
}

    //    /**
    //     * @return Activity[] Returns an array of Activity objects
    //     */
    //    public function findByExampleField($value): array
    //    {
    //        return $this->createQueryBuilder('a')
    //            ->andWhere('a.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->orderBy('a.id', 'ASC')
    //            ->setMaxResults(10)
    //            ->getQuery()
    //            ->getResult()
    //        ;
    //    }

    //    public function findOneBySomeField($value): ?Activity
    //    {
    //        return $this->createQueryBuilder('a')
    //            ->andWhere('a.exampleField = :val')
    //            ->setParameter('val', $value)
    //            ->getQuery()
    //            ->getOneOrNullResult()
    //        ;
    //    }
}
