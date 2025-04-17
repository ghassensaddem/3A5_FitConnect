<?php

namespace App\Repository;

use App\Entity\Coach;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

class CoachRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Coach::class);
    }

    public function afficher(): array
    {
        return $this->findAll();
    }
    public function supprimer(Coach $coach): void
    {
        $em = $this->getEntityManager();
        $em->remove($coach);
        $em->flush();
    }
    public function modifier(Coach $coach): void
    {
        $em = $this->getEntityManager();
        $em->flush();
    }
    public function ajouter(Coach $coach): void
    {
        $em = $this->getEntityManager();
        $em->persist($coach);
        $em->flush();
    }


    public function updateImage(Coach $coach): void
    {
        $queryBuilder = $this->createQueryBuilder('c')
            ->update(Coach::class, 'c')
            ->set('c.image', ':image')
            ->where('c.id = :id')
            ->setParameter('image', $coach->getImage())
            ->setParameter('id', $coach->getId());
        $queryBuilder->getQuery()->execute();
    }

    // Add custom methods as needed
}