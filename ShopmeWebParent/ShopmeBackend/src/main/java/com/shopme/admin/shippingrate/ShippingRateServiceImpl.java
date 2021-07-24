package com.shopme.admin.shippingrate;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShippingRateServiceImpl implements ShippingRateService {

  private ShippingRateRepository shippingRateRepository;

  private CountryRepository countryRepository;

  @Autowired
  public ShippingRateServiceImpl(
      ShippingRateRepository shippingRateRepository, CountryRepository countryRepository) {
    this.shippingRateRepository = shippingRateRepository;
    this.countryRepository = countryRepository;
  }

  @Override
  public void listByPage(int pageNum, PagingAndSortingHelper pagingAndSortingHelper) {
    pagingAndSortingHelper.listEntities(
        pageNum, ShippingRateConstants.RATES_PER_PAGE, shippingRateRepository);
  }

  @Override
  public List<Country> listAllCountries() {
    return countryRepository.findAllByOrderByNameAsc();
  }

  @Override
  public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
    ShippingRate rateInDB =
        shippingRateRepository.findByCountryAndState(
            rateInForm.getCountry().getId(), rateInForm.getState());

    boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;
    boolean foundDifferentExistingRateInEditMode =
        rateInForm.getId() != null && rateInDB != null && !rateInDB.equals(rateInForm);

    if (foundExistingRateInNewMode || foundDifferentExistingRateInEditMode) {
      throw new ShippingRateAlreadyExistsException(
          "There's already a rate for the destination "
              + rateInForm.getCountry().getName()
              + ", "
              + rateInForm.getState());
    }
    shippingRateRepository.save(rateInForm);
  }

  @Override
  public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
    try {
      return shippingRateRepository.findById(id).get();
    } catch (NoSuchElementException ex) {
      throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
    }
  }

  @Override
  @Transactional
  public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
    Long count = shippingRateRepository.countById(id);
    if (count == null || count == 0) {
      throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
    }

    shippingRateRepository.updateCODSupport(id, codSupported);
  }

  @Override
  @Transactional
  public void delete(Integer id) throws ShippingRateNotFoundException {
    Long count = shippingRateRepository.countById(id);
    if (count == null || count == 0) {
      throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);

    }
    shippingRateRepository.deleteById(id);
  }
}