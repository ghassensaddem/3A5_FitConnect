<?php

namespace App\Form;

use App\Entity\Equipement;
use App\Entity\CategorieEquipement;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\Image;
use App\Form\DataTransformer\PriceTransformer;

class EquipementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nom', TextType::class, [
                'attr' => [
                    'class' => 'form-control',
                    'placeholder' => 'Nom de l\'équipement'
                ],
                'empty_data' => '',
                'required' => true
            ])
            ->add('description', TextareaType::class, [
                'attr' => ['class' => 'form-control', 'rows' => 4],
                'empty_data' => '',
                'required' => true
            ])
            ->add('prix', TextType::class, [
                'attr' => [
                    'class' => 'form-control',
                    'min' => '0'
                ],
                'empty_data' => null,
                'required' => true
            ])
            ->add('quantiteStock', IntegerType::class, [
                'attr' => [
                    'class' => 'form-control',
                    'min' => '0'
                ],
                'empty_data' => null,
                'required' => true
            ])
            ->add('categorie', EntityType::class, [
                'class' => CategorieEquipement::class,
                'choice_label' => 'nom',
                'attr' => ['class' => 'form-select'],
                'placeholder' => 'Sélectionnez une catégorie'
            ])
            ->add('imageFile', FileType::class, [
                'label' => 'Image de l\'équipement',
                'required' => false, // Important
                'mapped' => false,
          
            ]);

        $builder->get('prix')->addModelTransformer(new PriceTransformer());
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Equipement::class,
            'attr' => ['novalidate' => 'novalidate']
        ]);
    }
}