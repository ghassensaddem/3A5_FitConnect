<?php

namespace App\Entity;

use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;

use App\Entity\Client;
use Doctrine\Common\Collections\Collection;
use App\Entity\Commande_equipement;

#[ORM\Entity]
class Commande
{

    #[ORM\Id]
    #[ORM\Column(type: "integer")]
    private int $id;

        #[ORM\ManyToOne(targetEntity: Client::class, inversedBy: "commandes")]
    #[ORM\JoinColumn(name: 'clientId', referencedColumnName: 'id', onDelete: 'CASCADE')]
    private Client $clientId;

    #[ORM\Column(type: "string", length: 50)]
    private string $etat;

    #[ORM\Column(type: "string", length: 50)]
    private string $statutPaiement;

    #[ORM\Column(type: "integer")]
    private int $equipementId;

    #[ORM\Column(type: "integer")]
    private int $quantite;

    #[ORM\Column(type: "datetime")]
    private \DateTimeInterface $dateCreation;

    public function getId()
    {
        return $this->id;
    }

    public function setId($value)
    {
        $this->id = $value;
    }

    public function getClientId()
    {
        return $this->clientId;
    }

    public function setClientId($value)
    {
        $this->clientId = $value;
    }

    public function getEtat()
    {
        return $this->etat;
    }

    public function setEtat($value)
    {
        $this->etat = $value;
    }

    public function getStatutPaiement()
    {
        return $this->statutPaiement;
    }

    public function setStatutPaiement($value)
    {
        $this->statutPaiement = $value;
    }

    public function getEquipementId()
    {
        return $this->equipementId;
    }

    public function setEquipementId($value)
    {
        $this->equipementId = $value;
    }

    public function getQuantite()
    {
        return $this->quantite;
    }

    public function setQuantite($value)
    {
        $this->quantite = $value;
    }

    public function getDateCreation()
    {
        return $this->dateCreation;
    }

    public function setDateCreation($value)
    {
        $this->dateCreation = $value;
    }

    #[ORM\OneToMany(mappedBy: "commande_id", targetEntity: Commande_equipement::class)]
    private Collection $commande_equipements;

    public function __construct()
    {
        $this->commande_equipements = new ArrayCollection();
    }

    /**
     * @return Collection<int, Commande_equipement>
     */
    public function getCommandeEquipements(): Collection
    {
        return $this->commande_equipements;
    }

    public function addCommandeEquipement(Commande_equipement $commandeEquipement): static
    {
        if (!$this->commande_equipements->contains($commandeEquipement)) {
            $this->commande_equipements->add($commandeEquipement);
            $commandeEquipement->setCommandeId($this);
        }

        return $this;
    }

    public function removeCommandeEquipement(Commande_equipement $commandeEquipement): static
    {
        if ($this->commande_equipements->removeElement($commandeEquipement)) {
            // set the owning side to null (unless already changed)
            if ($commandeEquipement->getCommandeId() === $this) {
                $commandeEquipement->setCommandeId(null);
            }
        }

        return $this;
    }
}
