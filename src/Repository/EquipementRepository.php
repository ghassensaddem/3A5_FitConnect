<?php

namespace App\Repository;

use App\Entity\Equipement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Equipement>
 */
class EquipementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Equipement::class);
    }

    /*public function findByCategorySorted(int $categoryId): array
    {
        return $this->createQueryBuilder('e')
            ->andWhere('e.categorie = :categoryId')
            ->setParameter('categoryId', $categoryId)
            ->orderBy('e.nom', 'ASC')
            ->getQuery()
            ->getResult();
    }*/

// src/Repository/EquipementRepository.php

public function findByCategorieId(int $categorieId): array
{
    return $this->createQueryBuilder('e')
        ->innerJoin('e.categorie', 'c') // Jointure explicite
        ->addSelect('c') // Charge la catégorie en même temps
        ->andWhere('c.id = :id')
        ->setParameter('id', $categorieId)
        ->getQuery()
        ->getResult();
}
public function findByNomLike(string $nom): array
{
    return $this->createQueryBuilder('e')
        ->where('e.nom LIKE :nom')
        ->setParameter('nom', '%'.$nom.'%')
        ->orderBy('e.nom', 'ASC')
        ->getQuery()
        ->getResult();
}




}