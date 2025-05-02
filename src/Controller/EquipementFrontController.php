<?php

namespace App\Controller;
use App\Service\SmsGenerator;
use Psr\Log\LoggerInterface;
use App\Entity\Commande;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\ORM\EntityManagerInterface;
use App\Repository\CommandeRepository;
use App\Repository\ClientRepository;
use App\Entity\Equipement;
use App\Repository\EquipementRepository;
use App\Entity\CategorieEquipement;
use App\Repository\CategorieEquipementRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;
use Symfony\Component\HttpFoundation\JsonResponse;
use Stripe\Checkout\Session;
use Stripe\Stripe;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use App\Service\PdfGeneratorService;

final class EquipementFrontController extends AbstractController
{
   

    #[Route('/equipement/front/search', name: 'app_equipement_search')]
public function search(Request $request, EquipementRepository $equipementRepo): JsonResponse
{
    $searchTerm = $request->query->get('term', '');
    $equipements = $equipementRepo->findByNomLike($searchTerm);
    
    $results = [];
    foreach ($equipements as $equipement) {
        $results[] = [
            'id' => $equipement->getId(),
            'nom' => $equipement->getNom(),
            'image' => 'uploads/images/' . $equipement->getImage(),
            'categorie' => $equipement->getCategorie() ? $equipement->getCategorie()->getNom() : 'Non catégorisé',
            'prix' => number_format($equipement->getPrix(), 0, '.', '') . ' DT',
            'url' => $this->generateUrl('app_equipement_front_equipement', ['id' => $equipement->getId()])
        ];
    }
    
    return $this->json($results);
}
    #[Route('/equipement/front', name: 'app_equipement_front')]
    public function equipement(CategorieEquipementRepository $categorieRepo, EquipementRepository $equipementRepo): Response
    {
        return $this->render('equipement_front/equipement_front.html.twig', [
            'categories' => $categorieRepo->findAll(),
            'equipements' => $equipementRepo->findAll(), 
        ]);
    }


    #[Route('/equipement/front/categorie/{id}', name: 'app_equipement_by_categorie')]
    public function byCategorie(CategorieEquipementRepository $categorieRepo, EquipementRepository $equipementRepo,$id): Response
    {
        return $this->render('equipement_front/equipement_front.html.twig', [
            'categories' => $categorieRepo->findAll(),
           'equipements' => $equipementRepo->findByCategorieId($id),
            
        ]);
    }
    #[Route('/equipement/front/equipement/{id}', name: 'app_equipement_front_equipement')]
    public function equipement_specific(EquipementRepository $equipementRepo,$id): Response
    {
        $equipement = $equipementRepo->find($id);
    
        // Si l'auteur n'est pas trouvé, afficher une erreur
        if ($equipement === null) {
            throw $this->createNotFoundException('Equipement non trouvé.');
        }
        return $this->render('equipement_front/equipement_specific.html.twig', [
           'equipement' =>$equipement
        ]);
    }

    #[Route('/add-to-cart', name: 'app_add_to_cart', methods: ['POST'])]
public function addToCart(
    Request $request,
    EntityManagerInterface $entityManager,
    ClientRepository $clientRepository,
    EquipementRepository $equipementRepository,
    CommandeRepository $commandeRepository
): JsonResponse {
    $equipementId = $request->request->getInt('equipement_id');
    $quantity = $request->request->getInt('quantity');
    $clientId = 1; // À remplacer par l'ID du client connecté

    // Validation basique
    if ($equipementId <= 0 || $quantity <= 0) {
        return $this->json(['success' => false, 'message' => 'Données invalides.'], 400);
    }

    try {
        $client = $clientRepository->find($clientId);
        $equipement = $equipementRepository->find($equipementId);

        if (!$client) {
            return $this->json(['success' => false, 'message' => 'Client non trouvé.'], 404);
        }

        if (!$equipement) {
            return $this->json(['success' => false, 'message' => 'Équipement non trouvé.'], 404);
        }

        
        $existingCommande = $commandeRepository->findOneBy([
            'client' => $client,
            'equipement' => $equipement,
            'etat' => 'En attente'
        ]);

        if ($existingCommande) {
            return $this->json([
                'success' => false,
                'message' => 'L\'équipement "'.$equipement->getNom().'" existe déjà dans votre panier'
            ], 400);
        }

        // Vérification stock
        if ($quantity > $equipement->getQuantiteStock()) {
            return $this->json([
                'success' => false,
                'message' => 'Stock insuffisant. Quantité disponible: '.$equipement->getQuantiteStock()
            ], 400);
        }

    
        $commande = (new Commande())
            ->setClient($client)
            ->setEquipement($equipement)
            ->setEtat('En attente')
            ->setStatutPaiement('Non payé')
            ->setQuantite($quantity)
            ->setDateCreation(new \DateTime());

        $entityManager->persist($commande);
        $entityManager->flush();

        // Calcul de la quantité totale
        $totalQuantity = (int)$commandeRepository->createQueryBuilder('c')
            ->select('SUM(c.quantite)')
            ->where('c.client = :client')
            ->andWhere('c.etat = :etat')
            ->setParameter('client', $client)
            ->setParameter('etat', 'En attente')
            ->getQuery()
            ->getSingleScalarResult();

        return $this->json([
            'success' => true,
            'message' => 'L\'équipement "'.$equipement->getNom().'" a été ajouté au panier avec succès',
            'totalQuantity' => $totalQuantity
        ]);

    } catch (\Exception $e) {
        return $this->json([
            'success' => false,
            'message' => 'Une erreur technique est survenue. Veuillez réessayer.'
        ], 500);
    }
}

    
    #[Route('/panier', name: 'app_panier')]
public function panier(
    CommandeRepository $commandeRepository,
    ClientRepository $clientRepository
): Response {
   
    $user=$clientRepository->find(1);
    
    if (!$user) {
        return $this->redirectToRoute('app_login');
    }

    $commandes = $commandeRepository->findBy([
        'client' => $user,
        'etat' => 'En attente'
    ]);
    
   
    $total = array_reduce($commandes, function(float $sum, Commande $commande) {
        return $sum + ($commande->getEquipement()->getPrix() * $commande->getQuantite());
    }, 0);
    return $this->render('equipement_front/panier.html.twig', [
        'commandes' => $commandes,
        'total' => $total 
    ]);
}

#[Route('/update-quantity', name: 'app_update_quantity', methods: ['POST'])]
public function updateQuantity(
    Request $request, 
    CommandeRepository $commandeRepository,
    EntityManagerInterface $em
): JsonResponse {
    try {
        $data = json_decode($request->getContent(), true);
        
        $commande = $commandeRepository->find($data['id']);
        $equipement = $commande->getEquipement();
        $stockDisponible = $equipement->getQuantiteStock();

        // Si la quantité demandée > stock, on limite au stock disponible
        if ($data['quantity'] > $stockDisponible) {
            $commande->setQuantite($stockDisponible);
            $em->flush();

            return $this->json([
                'success' => false,
                'message' => 'Quantité limitée au stock disponible',
                'limitedQuantity' => $stockDisponible
            ]);
        }

        // Sinon traitement normal
        $commande->setQuantite($data['quantity']);
        $em->flush();

        return $this->json([
            'success' => true,
            'message' => 'Quantité mise à jour'
        ]);

    } catch (\Exception $e) {
        return $this->json([
            'success' => false,
            'message' => 'Erreur: ' . $e->getMessage()
        ], 500);
    }
}



#[Route('/remove-item/{id}', name: 'app_remove_item', methods: ['DELETE'])]
public function removeItem(
    Commande $commande,
    EntityManagerInterface $em
): JsonResponse {
    try {
        $em->remove($commande);
        $em->flush();

        return $this->json([
            'success' => true,
            'message' => 'Article supprimé du panier'
        ]);
    } catch (\Exception $e) {
        return $this->json([
            'success' => false,
            'message' => 'Erreur lors de la suppression'
        ], 500);
    }
}


#[Route('/clear-cart', name: 'app_clear_cart', methods: ['DELETE'])]
public function clearCart(
    CommandeRepository $commandeRepository,
    ClientRepository $clientRepository, // <-- Ajoutez cette injection
    EntityManagerInterface $em
): JsonResponse {
    try {
        // Récupération du client (version temporaire avec find(1))
        $user = $clientRepository->find(1);
        if (!$user) {
            return $this->json([
                'success' => false,
                'message' => 'Client non trouvé'
            ], 404);
        }

        // Récupération des commandes
        $commandes = $commandeRepository->findBy([
            'client' => $user, 
            'etat' => 'En attente'
        ]);

        // Suppression
        foreach ($commandes as $commande) {
            $em->remove($commande);
        }
        
        $em->flush();

        return $this->json([
            'success' => true,
            'message' => 'Panier vidé avec succès',
            'count' => 0
        ]);

    } catch (\Exception $e) {
        // Log l'erreur pour le débogage
        error_log('Erreur clearCart: ' . $e->getMessage());
        
        return $this->json([
            'success' => false,
            'message' => 'Erreur lors de la suppression',
            'error' => $e->getMessage() 
        ], 500);
    }
}


#[Route('/cart-count', name: 'app_cart_count')]
public function getCartCount(CommandeRepository $commandeRepository, ClientRepository $clientRepository): Response
{
    $client = $clientRepository->find(1);
    $total = $client ? $commandeRepository->sumQuantitiesForClient($client) : 0;
    
    return new Response((string)$total, 200, [
        'Cache-Control' => 'no-cache'
    ]);
}



#[Route('/passer-commande', name: 'app_passer_commande', methods: ['POST'])]
public function passerCommande(
    ClientRepository $clientRepository,
    CommandeRepository $commandeRepository,
    EntityManagerInterface $entityManager
): Response {
    try {
        $client = $clientRepository->find(1); // À remplacer par $this->getUser() en production
        
        if (!$client) {
            $this->addFlash('error', 'Client non trouvé');
            return $this->redirectToRoute('app_panier');
        }

        // Récupérer toutes les commandes en attente
        $commandes = $commandeRepository->findBy([
            'client' => $client,
            'etat' => 'En attente'
        ]);

        if (empty($commandes)) {
            $this->addFlash('error', 'Votre panier est vide');
            return $this->redirectToRoute('app_panier');
        }

        // Mettre à jour chaque commande
        foreach ($commandes as $commande) {
            $commande->setEtat('En commande');
            $commande->setDateCreation(new \DateTime()); // Mise à jour de la date
            $entityManager->persist($commande);
        }

        $entityManager->flush();

        return $this->redirectToRoute('app_equipement_front_commande');

    } catch (\Exception $e) {
        $this->addFlash('error', 'Une erreur est survenue lors de la commande');
        return $this->redirectToRoute('app_panier');
    }
}

#[Route('/commande', name: 'app_equipement_front_commande')]
public function afficherCommande(SmsGenerator $smsGenerator,ClientRepository $clientRepository,CommandeRepository $commandeRepository): Response
{
    $client = $clientRepository->find(1);
    
    if (!$client) {
        return $this->redirectToRoute('app_login');
    }

    $commandes = $commandeRepository->findBy([
        'client' => $client,
        'etat' => 'En commande'
    ], ['dateCreation' => 'DESC']);

    if (empty($commandes)) {
        return $this->redirectToRoute('app_panier');
    }
    
     $dateLimite = (new \DateTime())->add(new \DateInterval('P7D'));
     $dateFormatee = $dateLimite->format('d/m/Y');
     $message = sprintf(
        "Bonjour %s, votre commande a été confirmée. Vous devez payer avant le %s.",
        $client->getNom(),
        $dateFormatee
    );
    $smsGenerator->sendSms(
        "+21627073247", // Numéro du client
        $client->getNom(),
        $message
    );

 
    $total = 0;
    foreach ($commandes as $commande) {
        if ($commande->getEquipement()) {
            $total += $commande->getEquipement()->getPrix() * $commande->getQuantite();
        }
    }

    return $this->render('equipement_front/commande.html.twig', [
        'commandes' => $commandes,
        'client' => $client,
        'total' => $total,
        'dateCommande' => $commandes[0]->getDateCreation(), // date de la première commande
    ]);
}
#[Route('/checkout', name: 'checkout')]
public function checkout(
    ClientRepository $clientRepository,
    CommandeRepository $commandeRepository,
    EntityManagerInterface $entityManager
): Response {
    Stripe::setApiKey('sk_test_51QxQ0L013eLvCqd8X9t4TvnvwixFB37Dp9vmqCKckowtzns31pI4aS1wRMqyLSyG4HHdebz8iYeRsieM41Nyt0E300y297MPiL');

    // Récupérer le client (à remplacer par l'utilisateur connecté en production)
    $client = $clientRepository->find(1);
    if (!$client) {
        $this->addFlash('error', 'Client non trouvé');
        return $this->redirectToRoute('app_panier');
    }

    $commandes = $commandeRepository->findBy([
        'client' => $client,
        'etat' => 'En commande'
    ]);

  

    // Préparer les line_items pour Stripe
    $lineItems = [];
    foreach ($commandes as $commande) {
        $equipement = $commande->getEquipement();
        if (!$equipement) {
            continue;
        }

        $lineItems[] = [
            'price_data' => [
                'currency' => 'usd', 
                'product_data' => [
                    'name' => $equipement->getNom(),
                    
                ],
                'unit_amount' => $equipement->getPrix() * 100, 
            ],
            'quantity' => $commande->getQuantite(),
        ];
    }

    // Créer la session Stripe
    $session = Session::create([
        'payment_method_types' => ['card'],
        'line_items' => $lineItems,
        'mode' => 'payment',
        'success_url' => $this->generateUrl('success_url', [], UrlGeneratorInterface::ABSOLUTE_URL),
        'cancel_url' => $this->generateUrl('cancel_url', [], UrlGeneratorInterface::ABSOLUTE_URL),
       
    ]);

    return $this->redirect($session->url, 303);
}

#[Route('/success-url', name: 'success_url')]
public function successUrl(
    ClientRepository $clientRepository,
    CommandeRepository $commandeRepository,
    EquipementRepository $equipementRepository,
    EntityManagerInterface $entityManager,
    SmsGenerator $smsGenerator
): Response {
    // Récupérer le client (à remplacer par $this->getUser() en production)
    $client = $clientRepository->find(1);
    if (!$client) {
        $this->addFlash('error', 'Client non trouvé');
        return $this->redirectToRoute('app_panier');
    }

    // Récupérer les commandes payées
    $commandes = $commandeRepository->findBy([
        'client' => $client,
        'etat' => 'En commande'
    ]);

    if (empty($commandes)) {
        $this->addFlash('error', 'Aucune commande trouvée');
        return $this->redirectToRoute('app_panier');
    }

    try {
        // Traiter chaque commande
        foreach ($commandes as $commande) {
            $equipement = $commande->getEquipement();
            
            if ($equipement) {
                // Mettre à jour le stock
                $nouvelleQuantite = $equipement->getQuantiteStock() - $commande->getQuantite();
                $equipement->setQuantiteStock($nouvelleQuantite);
                
                // Supprimer l'équipement si stock épuisé
                if ($nouvelleQuantite <= 0) {
                    $entityManager->remove($equipement);
                } else {
                    $entityManager->persist($equipement);
                }
                
                // Mettre à jour le statut de la commande
                $commande->setEtat('Livraison');
                $commande->setStatutPaiement('Payé');
                $entityManager->persist($commande);
            }
        }

        $entityManager->flush();
         return $this->render('equipement_front/success.html.twig', []);

    } catch (\Exception $e) {
        $this->addFlash('error', 'Une erreur est survenue lors du traitement de votre commande');
        return $this->redirectToRoute('app_panier');
    }
}


    #[Route('/cancel-url', name: 'cancel_url')]
    public function cancelUrl(): Response
    {
        return $this->render('equipement_front/cancel.html.twig', []);
    }

    #[Route('/commande/pdf', name: 'app_commande_pdf')]
    public function generatePdf(ClientRepository $clientRepository,
        CommandeRepository $commandeRepository,
        PdfGeneratorService $pdfGenerator
    ): Response {
        $client = $clientRepository->find(1);
        $commandes = $commandeRepository->findBy([
            'client' => $client,
            'etat' => 'En commande'
        ], ['dateCreation' => 'DESC']);
    
        if (empty($commandes)) {
            return $this->redirectToRoute('app_panier');
        }
    
        
        $total = 0;
        foreach ($commandes as $commande) {
            if ($commande->getEquipement()) {
                $total += $commande->getEquipement()->getPrix() * $commande->getQuantite();
            }
        }
    
        // Rendre le HTML pour le PDF
        $html = $this->renderView('equipement_front/pdf_commande.html.twig', [
            'commandes' => $commandes,
            'client' => $client,
            'total' => $total,
            'dateCommande' => $commandes[0]->getDateCreation(),
        ]);
    
        // Générer le PDF
        return $pdfGenerator->generatePdfFromHtml($html);
    }









}
