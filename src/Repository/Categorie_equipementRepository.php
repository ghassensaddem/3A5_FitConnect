<?php

namespace App\Repository;

use App\Entity\Categorie_equipement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class Categorie_equipementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Categorie_equipement::class);
    }

    // Add custom methods as needed
}