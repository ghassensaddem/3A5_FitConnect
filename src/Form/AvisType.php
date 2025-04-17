<?php

namespace App\Form;

use App\Entity\Avis;
use App\Entity\Seance;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class AvisType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('commentaire', null, [
            'label' => 'Commentaire',
            'attr' => ['placeholder' => 'Entrez votre commentaire'],
            'required' => true
        ])
        
        ->add('note', null, [
            'label' => 'Note',
            'label_attr' => ['class' => 'label-grand'], // 👈 pareil ici
            'attr' => ['placeholder' => 'Attribuez une note'],
            'required' => true
        ]);
    
    }
    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Avis::class,
        ]);
    }
}
   

