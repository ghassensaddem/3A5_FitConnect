<?php

namespace App\Form;

use App\Entity\Sallesportif;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\TimeType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;


class SallesportifType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nomSalle', TextType::class, [
                'label' => 'Nom de la salle',
                'attr' => [
                    'class' => 'form-control',
                    'placeholder' => 'Entrez le nom de la salle'
                ]
            ])
            ->add('addresseSalle', ChoiceType::class, [
                'label' => 'Adresse',
                'choices' => array_combine(Sallesportif::ADDRESSES, Sallesportif::ADDRESSES),
                'attr' => [
                    'class' => 'form-control'
                ],
                'placeholder' => 'Choisir une adresse'
            ])
            ->add('horaireOuverture', TimeType::class, [
                'label' => 'Heure d\'ouverture',
                'widget' => 'single_text',
                'attr' => [
                    'class' => 'form-control'
                ]
            ])
            ->add('horaireFermeture', TimeType::class, [
                'label' => 'Heure de fermeture',
                'widget' => 'single_text',
                'attr' => [
                    'class' => 'form-control'
                ]
            ])
            ->add('capacity', IntegerType::class, [
                'label' => 'Capacité',
                'attr' => [
                    'class' => 'form-control',
                    'placeholder' => 'Nombre maximum de personnes'
                ]
            ])
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Sallesportif::class,
        ]);
    }
}