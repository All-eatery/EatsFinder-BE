import { PrismaClient } from '@prisma/client';
import * as fs from 'fs';

async function createPlace() {
  const prisma = new PrismaClient();
  console.log('Start createPlace');

  try {
    const place = await prisma.places.findMany();
    if (place.length !== 0) {
      throw new Error('이미 데이터가 있습니다.');
    }
    const places = JSON.parse(fs.readFileSync('src/main/nest/script/json/place.json', 'utf-8'));

    for (const place of places) {
      const {
        address_name,
        category_name,
        category_group_name,
        category_group_code,
        place_name,
        phone,
        road_address_name,
        x,
        y,
      } = place;

      let category = await prisma.categories.findFirst({
        where: { name: category_name.split('>')[1].trim() },
      });

      if (!category) {
        category = await prisma.categories.create({
          data: {
            name: category_name.split('>')[1].trim(),
            type: category_group_name,
            code: category_group_code,
          },
        });
      }

      await prisma.places.create({
        data: {
          name: place_name,
          address: address_name,
          roadAddress: road_address_name,
          telephone: phone,
          x: Number(x),
          y: Number(y),
          categoryId: category.id,
        },
      });
    }
  } catch (e) {
    console.log(e);
  } finally {
    await prisma.$disconnect();
    console.log('Prisma client disconnect');
  }
}

createPlace();
