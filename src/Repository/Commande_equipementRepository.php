<?php

namespace App\Repository;

use App\Entity\Commande_equipement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class Commande_equipementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Commande_equipement::class);
    }

    // Add custom methods as needed
}