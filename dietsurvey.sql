/**********************************************************************
 * Title: Diet-Survey
 * Description: Application for calculating nutrient intake
 * Author: Agnibho Mondal
 * Website: http://code.agnibho.com
 **********************************************************************
   Copyright (c) 2013-2015 Agnibho Mondal
   All rights reserved
 **********************************************************************
   This file is part of Diet-Survey.
   
   Diet-Survey is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.
   
   Diet-Survey is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   
   You should have received a copy of the GNU General Public License
   along with Diet-Survey.  If not, see <http://www.gnu.org/licenses/>.
 **********************************************************************/
-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 03, 2015 at 09:02 PM
-- Server version: 10.0.22-MariaDB-log
-- PHP Version: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dietsurvey`
--

-- --------------------------------------------------------

--
-- Table structure for table `food`
--

CREATE TABLE `food` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `food_group` varchar(16) NOT NULL,
  `energy` decimal(6,2) NOT NULL,
  `protein` decimal(6,2) NOT NULL,
  `fat` decimal(6,2) NOT NULL,
  `iron` decimal(6,2) NOT NULL,
  `calcium` decimal(6,2) NOT NULL,
  `vitA` decimal(6,2) NOT NULL,
  `vitC` decimal(6,2) NOT NULL,
  `thiamine` decimal(6,2) NOT NULL,
  `riboflavin` decimal(6,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `food`
--

INSERT INTO `food` (`id`, `name`, `food_group`, `energy`, `protein`, `fat`, `iron`, `calcium`, `vitA`, `vitC`, `thiamine`, `riboflavin`) VALUES
(1, 'Rice', 'Cereals', '348.00', '6.40', '0.40', '1.00', '9.00', '0.00', '0.00', '0.21', '0.05'),
(2, 'Wheat', 'Cereals', '341.00', '12.10', '1.70', '4.90', '48.00', '29.00', '0.00', '0.49', '0.17'),
(3, 'Dal', 'Pulses', '348.00', '24.50', '1.20', '3.90', '75.00', '49.00', '0.00', '0.47', '0.21'),
(4, 'Lentil', 'Pulses', '343.00', '25.10', '0.60', '7.60', '69.00', '270.00', '0.00', '0.45', '0.20'),
(5, 'Soyabean', 'Pulses', '432.00', '43.20', '19.50', '10.40', '240.00', '426.00', '0.00', '0.73', '0.39'),
(6, 'Ground Nut', 'Nuts & Oilseeds', '567.00', '25.30', '40.10', '2.50', '90.00', '37.00', '0.00', '0.90', '0.13'),
(7, 'Amaranth', 'GLV', '45.00', '4.00', '0.50', '3.49', '397.00', '5520.00', '99.00', '0.03', '0.30'),
(8, 'Cabbage', 'GLV', '27.00', '1.80', '0.10', '0.80', '39.00', '120.00', '124.00', '0.06', '0.09'),
(9, 'Spinach', 'GLV', '26.00', '2.00', '0.70', '1.14', '73.00', '5580.00', '28.00', '0.03', '0.25'),
(10, 'Bottle Guard', 'Other Vegetables', '12.00', '0.20', '0.10', '0.46', '20.00', '0.00', '0.00', '0.03', '0.01'),
(11, 'Brinjal', 'Other Vegetables', '24.00', '1.40', '0.30', '0.38', '18.00', '74.00', '12.00', '0.04', '0.11'),
(12, 'Cauliflower', 'Other Vegetables', '30.00', '2.60', '0.40', '1.23', '33.00', '30.00', '56.00', '0.04', '0.10'),
(13, 'Cucumber', 'Other Vegetables', '13.00', '0.40', '0.10', '0.60', '10.00', '0.00', '7.00', '0.03', '0.00'),
(14, 'Ladies Finger', 'Other Vegetables', '35.00', '1.90', '0.20', '0.35', '66.00', '52.00', '13.00', '0.07', '0.10'),
(15, 'Papaya (green)', 'Other Vegetables', '27.00', '0.70', '0.20', '0.90', '28.00', '0.00', '12.00', '0.00', '0.00'),
(16, 'Parwar', 'Other Vegetables', '20.00', '2.00', '0.30', '1.70', '30.00', '153.00', '29.00', '0.50', '0.60'),
(17, 'Pumpkin', 'Other Vegetables', '25.00', '1.40', '0.10', '0.44', '10.00', '50.00', '2.00', '0.60', '0.40'),
(18, 'Plantain', 'Other Vegetables', '64.00', '1.40', '0.20', '6.27', '10.00', '30.00', '24.00', '0.05', '0.02'),
(19, 'Carrot', 'Roots & Tubers', '48.00', '0.90', '0.20', '1.03', '80.00', '1890.00', '3.00', '0.04', '0.02'),
(20, 'Onion', 'Roots & Tubers', '50.00', '1.20', '0.10', '0.60', '47.00', '0.00', '11.00', '0.06', '0.01'),
(21, 'Potato', 'Roots & Tubers', '97.00', '1.60', '0.10', '0.48', '10.00', '24.00', '17.00', '0.10', '0.01'),
(22, 'Radish', 'Roots & Tubers', '32.00', '0.60', '0.30', '0.37', '50.00', '3.00', '17.00', '0.06', '0.02'),
(23, 'Amla', 'Fruits', '58.00', '0.50', '0.10', '1.20', '50.00', '9.00', '600.00', '0.03', '0.01'),
(24, 'Apple', 'Fruits', '59.00', '0.20', '0.66', '1.00', '10.00', '0.00', '1.00', '0.00', '0.00'),
(25, 'Banana', 'Fruits', '116.00', '1.20', '0.36', '0.90', '17.00', '78.00', '7.00', '0.05', '0.08'),
(26, 'Grape', 'Fruits', '71.00', '0.50', '0.30', '0.50', '20.00', '0.00', '1.00', '0.00', '0.00'),
(27, 'Lemon', 'Fruits', '57.00', '1.00', '0.90', '0.26', '70.00', '0.00', '39.00', '0.02', '0.01'),
(28, 'Guava', 'Fruits', '51.00', '0.90', '0.30', '0.27', '10.00', '0.00', '212.00', '0.03', '0.03'),
(29, 'Mango', 'Fruits', '74.00', '0.60', '0.40', '1.30', '14.00', '2743.00', '16.00', '0.06', '0.09'),
(30, 'Orange', 'Fruits', '48.00', '0.70', '0.20', '0.32', '26.00', '1104.00', '30.00', '0.00', '0.00'),
(31, 'Papaya (ripe)', 'Fruits', '32.00', '0.60', '0.10', '0.50', '17.00', '666.00', '57.00', '0.04', '0.25'),
(32, 'Tomato', 'Fruits', '20.00', '0.90', '0.20', '0.64', '48.00', '351.00', '27.00', '0.12', '0.06'),
(33, 'Milk (cow)', 'Milk & Products', '67.00', '3.20', '4.10', '0.20', '120.00', '53.00', '2.00', '0.05', '0.19'),
(34, 'Milk (human)', 'Milk & Products', '65.00', '1.10', '3.40', '0.00', '28.00', '41.00', '3.00', '0.02', '0.02'),
(35, 'Egg', 'Flesh Foods', '173.00', '13.30', '13.30', '2.10', '60.00', '420.00', '0.00', '0.10', '0.40'),
(36, 'Fish (rohu)', 'Flesh Foods', '92.00', '16.60', '1.40', '1.00', '650.00', '0.00', '22.00', '0.05', '0.07'),
(37, 'Chillies', 'Spices', '29.00', '2.90', '0.60', '4.40', '30.00', '175.00', '111.00', '0.19', '0.39'),
(38, 'Oil', 'Fats & Oils', '900.00', '0.00', '100.00', '0.00', '0.00', '0.00', '0.00', '0.00', '0.00'),
(39, 'Sugar', 'Sugar & Jaggery', '398.00', '0.10', '0.00', '0.16', '12.00', '0.00', '0.00', '0.00', '0.00');

-- --------------------------------------------------------

--
-- Table structure for table `rda`
--

CREATE TABLE `rda` (
  `id` int(11) NOT NULL,
  `subject` varchar(16) NOT NULL,
  `energy` decimal(6,2) NOT NULL,
  `protein` decimal(6,2) NOT NULL,
  `iron` decimal(6,2) NOT NULL,
  `vitA` decimal(6,2) NOT NULL,
  `thiamine` decimal(6,2) NOT NULL,
  `riboflavin` decimal(6,2) NOT NULL,
  `vitC` decimal(6,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rda`
--

INSERT INTO `rda` (`id`, `subject`, `energy`, `protein`, `iron`, `vitA`, `thiamine`, `riboflavin`, `vitC`) VALUES
(1, 'infant_0m-6m', '500.00', '0.00', '0.00', '0.00', '0.20', '0.30', '25.00'),
(2, 'infant_6m-9m', '670.00', '13.40', '5.00', '2100.00', '0.30', '0.40', '25.00'),
(3, 'infant_9m-12m', '670.00', '14.90', '5.00', '2100.00', '0.30', '0.40', '25.00'),
(4, 'male_1y', '1060.00', '15.10', '9.00', '3200.00', '0.50', '0.60', '40.00'),
(5, 'male_2y', '1060.00', '16.00', '9.00', '3200.00', '0.50', '0.60', '40.00'),
(6, 'male_3y', '1060.00', '17.20', '9.00', '3200.00', '0.50', '0.60', '40.00'),
(7, 'male_4y', '1350.00', '18.30', '13.00', '3200.00', '0.70', '0.80', '40.00'),
(8, 'male_5y', '1350.00', '19.80', '13.00', '3200.00', '0.70', '0.80', '40.00'),
(9, 'male_6y', '1350.00', '23.50', '13.00', '3200.00', '0.70', '0.80', '40.00'),
(10, 'male_7y', '1690.00', '26.60', '16.00', '4800.00', '0.80', '1.00', '40.00'),
(11, 'male_8y', '1690.00', '29.70', '16.00', '4800.00', '0.80', '1.00', '40.00'),
(12, 'male_9y', '1690.00', '33.00', '16.00', '4800.00', '0.80', '1.00', '40.00'),
(13, 'male_10y', '2190.00', '36.30', '21.00', '4800.00', '1.10', '1.30', '40.00'),
(14, 'male_11y', '2190.00', '39.60', '21.00', '4800.00', '1.10', '1.30', '40.00'),
(15, 'male_12y', '2190.00', '43.70', '21.00', '4800.00', '1.10', '1.30', '40.00'),
(16, 'male_13y', '2750.00', '49.80', '32.00', '4800.00', '1.40', '1.60', '40.00'),
(17, 'male_14y', '2750.00', '54.70', '32.00', '4800.00', '1.40', '1.60', '40.00'),
(18, 'male_15y', '2750.00', '58.20', '32.00', '4800.00', '1.40', '1.60', '40.00'),
(19, 'male_16y', '3020.00', '60.80', '28.00', '4800.00', '1.50', '1.80', '40.00'),
(20, 'male_17y', '3020.00', '62.20', '28.00', '4800.00', '1.50', '1.80', '40.00'),
(21, 'female_1y', '1060.00', '14.10', '9.00', '3200.00', '0.50', '0.30', '40.00'),
(22, 'female_2y', '1060.00', '15.10', '9.00', '3200.00', '0.50', '0.30', '40.00'),
(23, 'female_3y', '1060.00', '16.80', '9.00', '3200.00', '0.50', '0.30', '40.00'),
(24, 'female_4y', '1350.00', '17.80', '13.00', '3200.00', '0.70', '0.80', '40.00'),
(25, 'female_5y', '1350.00', '19.30', '13.00', '3200.00', '0.70', '0.80', '40.00'),
(26, 'female_6y', '1350.00', '23.00', '13.00', '3200.00', '0.70', '0.80', '40.00'),
(27, 'female_7y', '1690.00', '26.10', '16.00', '4800.00', '0.80', '1.00', '40.00'),
(28, 'female_8y', '1690.00', '29.50', '16.00', '4800.00', '0.80', '1.00', '40.00'),
(29, 'female_9y', '1690.00', '32.60', '16.00', '4800.00', '0.80', '1.00', '40.00'),
(30, 'female_10y', '2010.00', '36.80', '27.00', '4800.00', '1.00', '1.20', '40.00'),
(31, 'female_11y', '2010.00', '40.00', '27.00', '4800.00', '1.00', '1.20', '40.00'),
(32, 'female_12y', '2010.00', '44.50', '27.00', '4800.00', '1.00', '1.20', '40.00'),
(33, 'female_13y', '2330.00', '49.00', '27.00', '4800.00', '1.20', '1.40', '40.00'),
(34, 'female_14y', '2330.00', '52.80', '27.00', '4800.00', '1.20', '1.40', '40.00'),
(35, 'female_15y', '2330.00', '53.80', '27.00', '4800.00', '1.20', '1.40', '40.00'),
(36, 'female_16y', '2440.00', '54.90', '26.00', '4800.00', '1.00', '1.20', '40.00'),
(37, 'female_17y', '2440.00', '56.00', '26.00', '4800.00', '1.00', '1.20', '40.00'),
(38, 'male_sedentary', '2320.00', '60.00', '17.00', '4800.00', '1.20', '1.40', '40.00'),
(39, 'male_moderate', '2730.00', '60.00', '17.00', '4800.00', '1.40', '1.60', '40.00'),
(40, 'male_heavy', '3490.00', '60.00', '17.00', '4800.00', '1.70', '2.10', '40.00'),
(41, 'female_sedentary', '1900.00', '55.00', '21.00', '4800.00', '1.00', '1.10', '40.00'),
(42, 'female_moderate', '2230.00', '55.00', '21.00', '4800.00', '1.10', '1.30', '40.00'),
(43, 'female_heavy', '2850.00', '55.00', '21.00', '4800.00', '1.40', '1.70', '40.00'),
(44, 'pregnant', '350.00', '78.00', '35.00', '6400.00', '0.20', '0.30', '60.00'),
(45, 'lactating_below6', '600.00', '74.00', '25.00', '7600.00', '0.30', '0.40', '80.00'),
(46, 'lactating_above6', '520.00', '68.00', '21.00', '7600.00', '0.20', '0.30', '80.00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `food`
--
ALTER TABLE `food`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rda`
--
ALTER TABLE `rda`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `food`
--
ALTER TABLE `food`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
--
-- AUTO_INCREMENT for table `rda`
--
ALTER TABLE `rda`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
