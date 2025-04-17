<?php

namespace App\Controller;

use App\Entity\CategorieActivity;
use App\Form\CategorieActivityType;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/categories')]
class CategorieActivityController extends AbstractController
{
    #[Route('/', name: 'app_categorie_list')]
    public function list(EntityManagerInterface $em): Response
    {
        $categories = $em->getRepository(CategorieActivity::class)->findAll();
        
        return $this->render('categorie_activity/index.html.twig', [
            'categorie_activities' => $categories,
        ]);
    }

    #[Route('/new', name: 'app_categorie_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $em): Response
    {
        $categorie = new CategorieActivity();
        $form = $this->createForm(CategorieActivityType::class, $categorie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->persist($categorie);
            $em->flush();

            $this->addFlash('success', 'Catégorie créée avec succès');
            return $this->redirectToRoute('app_categorie_list');
        }

        return $this->render('categorie_activity/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{idCategorie}', name: 'app_categorie_show', methods: ['GET'])]
    public function show(CategorieActivity $categorie): Response
    {
        return $this->render('categorie_activity/show.html.twig', [
            'categorie' => $categorie,
        ]);
    }

    #[Route('/{idCategorie}/edit', name: 'app_categorie_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, CategorieActivity $categorie, EntityManagerInterface $em): Response
    {
        $form = $this->createForm(CategorieActivityType::class, $categorie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $em->flush();

            $this->addFlash('success', 'Catégorie mise à jour avec succès');
            return $this->redirectToRoute('app_categorie_list');
        }

        return $this->render('categorie_activity/edit.html.twig', [
            'categorie' => $categorie,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{idCategorie}', name: 'app_categorie_delete', methods: ['POST'])]
    public function delete(Request $request, CategorieActivity $categorie, EntityManagerInterface $em): Response
    {
        if ($this->isCsrfTokenValid('delete'.$categorie->getIdCategorie(), $request->request->get('_token'))) {
            $em->remove($categorie);
            $em->flush();
            $this->addFlash('success', 'Catégorie supprimée avec succès');
        }

        return $this->redirectToRoute('app_categorie_list');
    }
}