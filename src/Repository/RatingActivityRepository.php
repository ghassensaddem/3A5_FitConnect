<?php

namespace App\Repository;

use App\Entity\RatingActivity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;
use App\Entity\Client;
use App\Entity\Activity;
use App\Entity\Sallesportif;

/**
 * @extends ServiceEntityRepository<RatingActivity>
 *
 * @method RatingActivity|null find($id, $lockMode = null, $lockVersion = null)
 * @method RatingActivity|null findOneBy(array $criteria, array $orderBy = null)
 * @method RatingActivity[]    findAll()
 * @method RatingActivity[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class RatingActivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, RatingActivity::class);
    }

    public function findUserRating(Client $client, Activity $activity, Sallesportif $salle): ?RatingActivity
    {
        return $this->createQueryBuilder('r')
            ->where('r.client = :client')
            ->andWhere('r.activity = :activity')
            ->andWhere('r.salle = :salle')
            ->setParameter('client', $client)
            ->setParameter('activity', $activity)
            ->setParameter('salle', $salle)
            ->getQuery()
            ->getOneOrNullResult();
    }
    
}