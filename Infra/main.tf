provider "aws" {
  region     = var.aws_region
  access_key = ""  
  secret_key = ""  
  token      = ""  
}

data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_security_group" "sg_frontend" {
  name   = "frontend-sg-rednorte"
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.admin_ip] 
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "sg_backend" {
  name   = "backend-sg-rednorte"
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.sg_frontend.id]
  }
  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    cidr_blocks     = [var.admin_ip] 
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "sg_data" {
  name   = "data-sg-rednorte"
  vpc_id = aws_vpc.main_vpc.id

  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.sg_backend.id]
  }
  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    cidr_blocks     = [var.admin_ip] 
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "frontend" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  subnet_id              = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_frontend.id]
  key_name               = var.key_name
  iam_instance_profile   = "LabInstanceProfile"

  tags = { Name = "RedNorte-EC2-Frontend" }

  user_data = <<-EOF
              #!/bin/bash
              apt-get update -y
              apt-get install -y nginx git curl
              systemctl start nginx
              systemctl enable nginx
              EOF
}

resource "aws_instance" "backend" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  subnet_id              = aws_subnet.private_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_backend.id]
  key_name               = var.key_name
  iam_instance_profile   = "LabInstanceProfile"

  tags = { Name = "RedNorte-EC2-Backend" }

  user_data = <<-EOF
              #!/bin/bash
              apt-get update -y
              apt-get install -y openjdk-21-jdk maven git curl
              EOF
}

resource "aws_instance" "data" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = var.instance_type
  subnet_id              = aws_subnet.private_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_data.id]
  key_name               = var.key_name
  iam_instance_profile   = "LabInstanceProfile"

  tags = { Name = "RedNorte-EC2-Data" }

  user_data = <<-EOF
              #!/bin/bash
              apt-get update -y
              apt-get install -y mysql-server
              
              # Configurar MySQL para aceptar conexiones remotas (desde el Backend)
              sed -i 's/bind-address.*/bind-address = 0.0.0.0/' /etc/mysql/mysql.conf.d/mysqld.cnf
              systemctl restart mysql

              # Crear las bases de datos de RedNorte
              mysql -e "CREATE DATABASE IF NOT EXISTS rednortedb;"
              mysql -e "CREATE DATABASE IF NOT EXISTS rednorte_espera;"
              mysql -e "CREATE DATABASE IF NOT EXISTS rednorte_notificaciones;"
              mysql -e "CREATE DATABASE IF NOT EXISTS rednorte_reasignaciones;"
              EOF
}