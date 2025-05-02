<?php

namespace App\Controller;
use App\Repository\CategorieEquipementRepository;
use App\Entity\Commande;
use App\Repository\CommandeRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\Routing\Attribute\Route;


final class CommandeController extends AbstractController
{
    #[Route('/afficher_commande', name: 'afficher_commande')]
    public function afficher_commande( CategorieEquipementRepository $categorieRepo,CommandeRepository $repository): Response
    {
        $a = $repository->findAll();
        $commandes = $repository->findBy([
            'statutPaiement' => 'Payé'
          
       ]);
       $quantiteParCategorie = [];
   
       foreach ($commandes as $commande) {
           $equipement = $commande->getEquipement();
          
               $categorieId = $equipement->getCategorie()->getId();
               $quantiteParCategorie[$categorieId] = ($quantiteParCategorie[$categorieId] ?? 0) + $commande->getQuantite();
           
       }
   
       $totalVentes = array_sum($quantiteParCategorie);
       $chartData = [];
       
   
       foreach ($quantiteParCategorie as $categorieId => $quantite) {
           $categorie = $categorieRepo->find($categorieId);
           if ($categorie) {
               $pourcentage = ($quantite * 100) / $totalVentes;
               $chartData[] = [
                   'label' => $categorie->getNom(),
                   'value' => round($pourcentage, 2),
                   'quantite' => $quantite,
                   'color' => $this->generateRandomColor() 
               ];
           }
       }

       return $this->render('commande/index.html.twig', [
        'commandes' => $a,
        'chartData' => $chartData
    ]);
    }
    // Fonction pour générer des couleurs aléatoires
    private function generateRandomColor(): string
    {
        return sprintf('#%06X', mt_rand(0, 0xFFFFFF));
    }

    #[Route('/delete_commande/{id}', name: 'delete_commande')]
    public function delete(EntityManagerInterface $em, int $id): Response
    {
        $commande = $em->getRepository(Commande::class)->find($id);
        
        if (!$commande) {
            throw $this->createNotFoundException('Commande non trouvée');
        }

        $em->remove($commande);
        $em->flush();

        return $this->redirectToRoute('afficher_commande');
    }


}
