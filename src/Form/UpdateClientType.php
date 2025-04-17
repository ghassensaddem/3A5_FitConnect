<?php

namespace App\Form;

use App\Entity\Client;
use App\Entity\Event;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;

class UpdateClientType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('nom', TextType::class, [
            'label' => 'Nom',
            'required' => false,
            'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez votre nom'],
        ])
        ->add('prenom', TextType::class, [
            'label' => 'Prénom',
            'required' => false,
            'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez votre prénom'],
        ])
        ->add('email', EmailType::class, [
            'label' => 'Email',
            'required' => false,
            'attr' => ['class' => 'form-control', 'placeholder' => 'Entrez votre email'],
        ])
        ->add('mdp', PasswordType::class, [
            'label' => 'Mot de passe',
            'required' => false,
            'mapped' => false,
            'attr' => ['class' => 'form-control', 'placeholder' => 'Choisissez un mdp'],
        ])
        ->add('dateNaissance', DateType::class, [
            'label' => 'Date de naissance',
            'required' => false,
            'mapped' => false,
            'widget' => 'single_text',
            'attr' => ['class' => 'form-control', 'placeholder' => 'Sélectionnez votre date de naissance'],
        ])
        ->add('poids', NumberType::class, [
            'label' => 'Poids (kg)',
            'required' => false,
            'scale' => 2, 
            'attr' => [
                'class' => 'form-control',
                'placeholder' => 'votre poids en kg'
            ],
        ])
        
        ->add('taille', NumberType::class, [
            'label' => 'Taille (cm)',
            'required' => false,
            'scale' => 2,
            'attr' => [
                'class' => 'form-control',
                'placeholder' => 'votre taille en cm'
            ],
        ]);
        
        
    
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Client::class,
            'attr' => ['novalidate' => 'novalidate'],
        ]);
    }
}
