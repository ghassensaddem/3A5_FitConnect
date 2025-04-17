<?php

namespace App\Repository;

use App\Entity\Activity;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class ActivityRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Activity::class);
    }

    
    public function afficherNomActivity()
    {
        return $this->createQueryBuilder('a')
            ->select('a.idActivity') // Sélectionne l'objet complet
            ->addSelect('a.nomActivity') // Sélectionne l'objet complet
            ->getQuery()
            ->getResult();
    }

}